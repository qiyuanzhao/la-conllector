package com.lavector.collector.crawler.project.weibo.weiboAllContent.newPage;

import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.nonce.NonceMessage;
import com.lavector.collector.crawler.util.JsonMapper;
import org.apache.commons.lang3.StringUtils;
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

public class AjaxFansMessagePage implements PageParse {

    private JsonMapper mapper = JsonMapper.buildNormalBinder();

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

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

        SkuData skuData = (SkuData) page.getRequest().getExtra("skuData");

        String data = JsonPath.read(page.getRawText(), "$.data");
        String owner = page.getRequest().getExtra("owner").toString();

        List<NonceMessage> messageAll = (List<NonceMessage>) page.getRequest().getExtra("messageAll");

        List<NonceMessage> messages = new ArrayList<>();
        if (data != null) {
            Html messageHtml = new Html(data);
            List<Selectable> messageNodes = messageHtml.xpath("//div[@action-type='feed_list_item']").nodes();
            messageNodes.stream()
                    .filter(node -> {
                        String time = node.xpath("//div[@class='WB_from S_txt2']/a[1]/@title").get();
                        return judgeTime(time);
                    })
                    .forEach(node -> {
                        String time = node.xpath("//div[@class='WB_from S_txt2']/a[1]/@date").get();
                        String text = node.xpath("//div[@class='WB_face W_fl']/div/a/@title").get();
                        String content = node.xpath("//div[@class='WB_text W_f14']/allText()").get();
                        String repost = node.xpath("//div[@class='WB_feed_handle']/div[@class='WB_handle']//span[@node-type='forward_btn_text']/span/em[2]/text()").get();
                        String comment = node.xpath("//div[@class='WB_feed_handle']/div[@class='WB_handle']//span[@node-type='comment_btn_text']/span/em[2]/text()").get();
                        String like = node.xpath("//div[@class='WB_feed_handle']/div[@class='WB_handle']//span[@node-type='like_status']/em[2]/text()").get();

                        NonceMessage.SocialCount socialCount = new NonceMessage.SocialCount();
                        socialCount.setComments(StringUtils.isNumeric(comment) ? Integer.parseInt(comment) : 0);
                        socialCount.setLikes(StringUtils.isNumeric(like) ? Integer.parseInt(like) : 0);
                        socialCount.setReposts(StringUtils.isNumeric(repost) ? Integer.parseInt(repost) : 0);

                        NonceMessage message = new NonceMessage();
                        message.setSocialCount(socialCount);
                        message.setContent(content);
                        message.setTime(format.format(new Date(Long.parseLong(time))));
                        message.setOwner(owner);

                        if (owner.compareTo(text)==0){
                            messages.add(message);
                        }
                    });

        }

        if (messageAll!=null&&messageAll.size()>0){
            messageAll.addAll(messages);
        }else {
            messageAll = messages;
        }

        // 分页
        Integer currentPage = Integer.parseInt(page.getUrl().regex("pagebar=(\\d+)").get());
        Integer pageStr = Integer.parseInt(page.getUrl().regex("&page=(\\d+)").get());
        if (pageStr<4){
            if (currentPage < 1) {
                Integer nextPage = currentPage + 1;
                String nextUrl = page.getUrl().get().replace("&pagebar=" + currentPage, "&pagebar=" + nextPage);
                result.addRequest(new us.codecraft.webmagic.Request(nextUrl).putExtra("owner", owner)
                        .putExtra("messageAll",messageAll)
                        .putExtra("skuData",skuData)
                        .putExtra("url",page.getRequest().getExtra("url"))
                );
            }else {
                Integer pageS = pageStr + 1;
                String nextUrl = page.getRequest().getExtra("url").toString().replace("&page=" + pageStr, "&page=" + pageS);
                result.addRequest(new us.codecraft.webmagic.Request(nextUrl).putExtra("owner", owner)
                        .putExtra("messageAll",messageAll)
                        .putExtra("skuData",skuData)
                );
            }
            page.setSkip(true);
        }else {
            page.putField("skuData",skuData);
            page.putField("messageAll",messageAll);
        }
        return result;
    }


    private boolean judgeTime(String time){
        String start = "2018-10-14";
        String end = "2018-11-16";
        boolean flag = false;
        try {
            Date startDate = format.parse(start);
            Date endDate = format.parse(end);
            Date timeDate = format.parse(time);
            flag = timeDate.after(startDate) && timeDate.before(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return flag;
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
