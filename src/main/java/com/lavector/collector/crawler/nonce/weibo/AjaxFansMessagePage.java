package com.lavector.collector.crawler.nonce.weibo;

import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.nonce.FileUtils;
import com.lavector.collector.crawler.nonce.NonceMessage;
import com.lavector.collector.crawler.util.JsonMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.client.fluent.Request;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created on 16/05/2018.
 *
 * @author zeng.zhao
 */
public class AjaxFansMessagePage implements PageParse {

    private JsonMapper mapper = JsonMapper.buildNormalBinder();

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public boolean handleUrl(String url) {
        return url.contains("weibo.com/p/aj/v6/mblog/mbloglist?domain");
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();

        String path = page.getRequest().getExtra("path").toString();
        String ownerId = page.getRequest().getExtra("userId").toString();


        String data = JsonPath.read(page.getRawText(), "$.data");
        String owner = page.getRequest().getExtra("owner").toString();
        List<NonceMessage> messages = new ArrayList<>();
        if (data != null) {
            Html messageHtml = new Html(data);
            List<Selectable> messageNodes = messageHtml.xpath("//div[@action-type='feed_list_item']").nodes();
            messageNodes.stream()
                    .filter(node -> {
                        String time = node.xpath("//div[@class='WB_from S_txt2']/a[1]/@title").get();
                        try {
                            Date date = DateUtils.parseDate(time, "yyyy-MM-dd HH:mm");
                            return date.after(FansMessageListPage.startDate) && date.before(FansMessageListPage.endDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return false;
                    })
                    .forEach(node -> {
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
                        message.setOwner(owner);

                        messages.add(message);
                    });
        }

//        if (messages.size() > 0) {
//            messages.forEach(message -> {
////                String s = mapper.toJson(message);
//                String url = "https://weibo.com/" + message.getUserId() + "/" + MidToUrlConverter.Mid2Uid(message.getMid());
//                String content = message.getContent().replace(",", "，") + "," +
//                        message.getTime() + "," +
//                        message.getOwner() + "," +
//                        url + "," +
//                        message.getSocialCount().getReposts() + "," +
//                        message.getSocialCount().getLikes() + "," +
//                        message.getSocialCount().getComments() + "," +
//                        ownerId;
////                FileUtils.write(content, "/Users/zeng.zhao/Desktop/weibo_message.json");
//                FileUtils.write(content, path);
//            });
//        }

        // 分页
        Integer currentPage = Integer.parseInt(page.getUrl().regex("pagebar=(\\d+)").get());
        if (currentPage < 1 && messages.size() > 0) {
            Integer nextPage = currentPage + 1;
            String nextUrl = page.getUrl().get().replace("&pagebar=" + currentPage, "&pagebar=" + nextPage);
            result.addRequest(new us.codecraft.webmagic.Request(nextUrl).putExtra("owner", owner).putExtra("path", path)
                    .putExtra("userId", ownerId)
            );
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        String url = "https://weibo.com/p/aj/v6/mblog/mbloglist?domain=100206&is_all=1&profile_ftype=1&page=1&pagebar=1&pl_name=Pl_Official_MyProfileFeed__28&id=1002065044281310&script_uri=/thepapernewsapp&feed_type=0&pre_page=1";
        String content = Request.Get(url)
                .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36")
                .addHeader("cookie", "SINAGLOBAL=9906983875306.838.1525775389863; UOR=,,login.sina.com.cn; un=13241300169; wvr=6; YF-Page-G0=324e50a7d7f9947b6aaff9cb1680413f; _s_tentry=login.sina.com.cn; Apache=4622720154015.327.1526442074285; ULV=1526442074292:8:8:3:4622720154015.327.1526442074285:1526353830278; YF-V5-G0=8d4d030c65d0ecae1543b50b93b47f0c; YF-Ugrow-G0=b02489d329584fca03ad6347fc915997; SCF=AjSsGNZHel-pUVITqhsDkD-KcZohKM--251X_qbCliPl-2P-5ZiczYUR_-7KfnVDHgiJVtD4rBQZFkD41IWMBTQ.; SUB=_2A253_60LDeRhGeNG7VMZ9S3EzT2IHXVUjJnDrDV8PUNbmtBeLVj6kW9NSy4l8yEdFmOcN-_9vp4xfDwXwE7-nEmr; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WWxKexMVNh2-Ia7fKQRNynk5JpX5K2hUgL.Fo-RSo2RSKeRSo22dJLoIEBLxKnL12qLBo2LxKqLB-BL12eLxKMLBKML1K2LxKqL122L1h5t; SUHB=0HTxClQJyZIao0; ALF=1527060442; SSOLoginState=1526455643")
                .execute()
                .returnContent()
                .asString();
        Page page = new Page();
        page.setRawText(content);
        page.setUrl(new Json(url));
        page.setRequest(new us.codecraft.webmagic.Request(url));
        AjaxFansMessagePage ajaxFansMessagePage = new AjaxFansMessagePage();
        ajaxFansMessagePage.parse(page);
    }
}
