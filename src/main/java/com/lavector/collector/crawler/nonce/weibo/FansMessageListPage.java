package com.lavector.collector.crawler.nonce.weibo;

import com.alibaba.fastjson.JSONPath;
import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.nonce.FileUtils;
import com.lavector.collector.crawler.nonce.NonceMessage;
import com.lavector.collector.crawler.project.edu.WriteFile;
import com.lavector.collector.crawler.util.JsonMapper;
import com.lavector.collector.crawler.util.RegexUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.client.fluent.Request;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * Created on 14/05/2018.
 *
 * @author zeng.zhao
 */
public class FansMessageListPage implements PageParse {

    //https://weibo.com/u/5737128197?is_all=1&profile_ftype=1&page=3

    public static Date startDate;

    public static Date endDate;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private JsonMapper mapper = JsonMapper.buildNormalBinder();

    static {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate startDate1 = LocalDate.of(2018, 6, 1);
        ZonedDateTime zonedDateTime = startDate1.atStartOfDay(zoneId);
        startDate = Date.from(zonedDateTime.toInstant());

        LocalDate endDate1 = LocalDate.of(2018, 11, 1);
        ZonedDateTime zonedDateTime1 = endDate1.atStartOfDay(zoneId);
        endDate = Date.from(zonedDateTime1.toInstant());
    }

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch(".*is_all=\\d\\&is_tag=\\d\\&profile_ftype=\\d\\&page=\\d+", url);
//        return url.contains("profile_ftype=");
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        Html html = page.getHtml();
        List<String> scripts = html.xpath("script/html()").all();
        String[] owner = new String[1];
        // 获取翻页请求所需要的参数，一页由三部分组成，第一部分页面源码，二三部分是ajax请求
        Map<String, String> params = new HashMap<>();
        scripts.stream().filter(s -> s.contains("var $CONFIG = {}")).findFirst().ifPresent(config -> {
            String pageId = new Json(config).regex("\\$CONFIG\\['page_id'\\]='(\\d+)';").get();
            String script = new Json(config).regex("\\$CONFIG\\['watermark'\\]='(u/\\d+)';").get();
            String domain = new Json(config).regex("\\$CONFIG\\['domain'\\]='(\\d+)';").get();
            owner[0] = new Json(config).regex("\\$CONFIG\\['onick'\\]='(.*?)';").get();
            params.put("pageId", pageId);
            params.put("script", script);
            params.put("domain", domain);
        });
        if (owner[0] == null) {
            owner[0] = html.xpath("//title/text()").regex("(.*)的微博").get();
        }


        String path = page.getRequest().getExtra("path").toString();
        String ownerId = page.getRequest().getExtra("userId").toString();
        String province = page.getRequest().getExtra("province").toString();

        //抓取follow
        String followUrl = "https://weibo.com/p/" + params.get("pageId") + "/follow?page=1";
        us.codecraft.webmagic.Request request = new us.codecraft.webmagic.Request(followUrl);
        request.putExtra("path", path);
        request.putExtra("ownerId", ownerId);
        request.putExtra("city", params.get("city"));
        request.putExtra("province", province);
        result.addRequest(request);
        if (followUrl.contains("follow")) {
            return result;
        }

        Optional<String> optionalS = scripts.stream().filter(s -> s.contains("\"pl.content.homeFeed.index\",\"domid\":\"Pl_Official_MyProfileFeed")).findFirst();
        List<NonceMessage> messages = new ArrayList<>();
        optionalS.ifPresent(s -> {
            String json = new Json(s).regex("FM.view\\((.*)\\)").get();
            String domId = JsonPath.read(json, "$.domid"); // ajax请求参数
            params.put("plName", domId);
            String htmlStr = JsonPath.read(json, "$.html");
            if (htmlStr != null) {
                Html messageHtml = new Html(htmlStr);
                List<Selectable> messageNodes = messageHtml.xpath("//div[@action-type='feed_list_item']").nodes();
                messageNodes.stream().filter(node -> {
                    String time = node.xpath("//div[@class='WB_from S_txt2']/a[1]/@title").get();
                    try {
                        Date date = DateUtils.parseDate(time, "yyyy-MM-dd HH:mm");
                        return date.after(startDate) && date.before(endDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return false;
                }).forEach(node -> {
                    String time = node.xpath("//div[@class='WB_from S_txt2']/a[1]/@date").get();
                    String userId = node.xpath("/div/@tbinfo").regex("ouid=(\\d+)").get();
                    String content = node.xpath("//div[@class='WB_text W_f14']/allText()").get();
                    String mid = node.xpath("//div[@class='WB_from S_txt2']/a[1]/@name").get();
                    String qmid = node.xpath("//div[@class='WB_expand S_bg1']/div[@class='WB_info']/a[1]/@suda-uatrack")
                            .regex("user_nick:(\\d+)").get();
                    String repost = node.xpath("//div[@class='WB_feed_handle']/div[@class='WB_handle']//span[@node-type='forward_btn_text']/span/em[2]/text()").get();
                    String comment = node.xpath("//div[@class='WB_feed_handle']/div[@class='WB_handle']//span[@node-type='comment_btn_text']/span/em[2]/text()").get();
                    String like = node.xpath("//div[@class='WB_feed_handle']/div[@class='WB_handle']//span[@node-type='like_status']/em[2]/text()").get();
                    String type = "RePost";
                    if (qmid == null) {
                        qmid = "";
                        type = "Post";
                    }

                    NonceMessage.SocialCount socialCount = new NonceMessage.SocialCount();
                    socialCount.setComments(StringUtils.isNumeric(comment) ? Integer.parseInt(comment) : 0);
                    socialCount.setLikes(StringUtils.isNumeric(like) ? Integer.parseInt(like) : 0);
                    socialCount.setReposts(StringUtils.isNumeric(repost) ? Integer.parseInt(repost) : 0);

                    NonceMessage message = new NonceMessage();
                    message.setContent(content);
                    message.setTime(format.format(new Date(Long.parseLong(time))));
                    message.setMid(mid);
                    message.setQmid(qmid);
                    message.setType(type);
                    message.setUserId(userId);
                    message.setSite("weibo");
                    message.setSocialCount(socialCount);
                    message.setOwner(owner[0]);

                    messages.add(message);
                });
            }
        });

        if (messages.size() > 0) {
            String currentPage = page.getUrl().regex("&page=(\\d+)").get();

            String domain = params.get("domain");
            String plName = params.get("plName");
            String id = params.get("pageId");
            String script_uri = params.get("script");
            String ajaxUrl = "https://weibo.com/p/aj/v6/mblog/mbloglist?domain=" + domain +
                    "&is_all=1&profile_ftype=1&page=" + currentPage + "&pagebar=0&pl_name=" + plName +
                    "&id=" + id + "&script_uri=" + script_uri + "&feed_type=0&pre_page=" + currentPage;
            result.addRequest(new us.codecraft.webmagic.Request(ajaxUrl).putExtra("owner", owner[0])
                    .putExtra("path", path)
                    .putExtra("userId", ownerId)
            );

            //下一页 .html
//            int nextPage = Integer.parseInt(currentPage) + 1;
//            String nextUrl = page.getUrl().get().replace("&page=" + currentPage, "&page=" + nextPage);
//            result.addRequest(new us.codecraft.webmagic.Request(nextUrl));

//            //保存 messages
//            messages.forEach(message -> {
////                String s = mapper.toJson(message);
//                if (!new File(path).exists()) {
//                    String head = "content,time,owner,url,repost,like,comment,ownerId";
//                    WriteFile.write(head, path);
//                }
//                String url = "https://weibo.com/" + message.getUserId() + "/" + MidToUrlConverter.Mid2Uid(message.getMid());
//                String content = message.getContent().replace(",", "，") + "," +
//                        message.getTime() + "," +
//                        message.getOwner() + "," +
//                        url + "," +
//                        message.getSocialCount().getReposts() + "," +
//                        message.getSocialCount().getLikes() + "," +
//                        message.getSocialCount().getComments() + "," +
//                        ownerId;
//                FileUtils.write(content, path);
//            });
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        String url = "https://weibo.com/u/1766688147?profile_ftype=1&is_all=1#_0";
        String content = Request.Get(url)
                .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36")
                .addHeader("cookie", "SINAGLOBAL=638387246136.2206.1527731616813; un=13241300169; wvr=6; UOR=,,login.sina.com.cn; YF-Ugrow-G0=5b31332af1361e117ff29bb32e4d8439; YF-V5-G0=a53c7b4a43414d07adb73f0238a7972e; _s_tentry=login.sina.com.cn; Apache=2381017761437.592.1539914700415; YF-Page-G0=8fee13afa53da91ff99fc89cc7829b07; ULV=1539914700449:63:10:5:2381017761437.592.1539914700415:1539850993698; wb_view_log_5842303108=1920*10801; SCF=AvXliNK2E1oyuXpLHOqFJwmipzSDe8bW4H38H0zcNHJ4PqFrKnI2O4kaNgHOun4Fv7ES5pt7oig9ZMCXek16Sa0.; SUB=_2A252zQXhDeRhGeNG7VMZ9S3EzT2IHXVVu3AprDV8PUNbmtAKLXTskW9NSy4l85FDNPLXabQfT8y4cewU6ECTJ0Ic; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WWxKexMVNh2-Ia7fKQRNynk5JpX5K2hUgL.Fo-RSo2RSKeRSo22dJLoIEBLxKnL12qLBo2LxKqLB-BL12eLxKMLBKML1K2LxKqL122L1h5t; SUHB=0OM0WSrqaCyxcU; ALF=1540534321; SSOLoginState=1539929521; wb_view_log_5861853861=1920*10801")
                .execute()
                .returnContent()
                .asString();
        Page page = new Page();
        page.setRequest(new us.codecraft.webmagic.Request(url));
        page.setRawText(content);
        page.setUrl(new Json(url));
        FansMessageListPage messageListPage = new FansMessageListPage();
        messageListPage.parse(page);
    }
}
