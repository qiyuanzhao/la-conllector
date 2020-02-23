package com.lavector.collector.crawler.nonce.weibo;

import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.nonce.NonceMessage;
import com.lavector.collector.crawler.project.edu.WriteFile;
import com.lavector.collector.crawler.util.JsonMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

/**
 * Created on 2018/9/3.
 *
 * @author zeng.zhao
 */
public class WeiBoCommentPage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return url.contains("weibo.com/aj/v6/comment/big?ajwvr=6");
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        String json = page.getJson().get();
        String htmlStr = JsonPath.read(json, "$.data.html");
        Html html = new Html(htmlStr);
        List<Selectable> nodes = html.xpath("//div[@class='list_li S_line1 clearfix']").nodes();
        nodes.forEach(node -> {
            String mid = node.xpath("/div/@comment_id").get();
            String content = node.xpath("//div[@class='WB_text']/text()").get();
            String commentUserName = node.xpath("//div[@class='WB_text']/a[1]/text()").get();
            String time = node.xpath("//div[@class='WB_from S_txt2']/text()").get();
            String likeNum = node.xpath("//span[@node-type='like_status']/em[2]/text()").get();

            NonceMessage nonceMessage = new NonceMessage();
            nonceMessage.setMid(mid);
            nonceMessage.setContent(content);
            nonceMessage.setTime(time);
            nonceMessage.setCommentUserName(commentUserName);

            NonceMessage.SocialCount socialCount = new NonceMessage.SocialCount();
            try {
                socialCount.setLikes(Integer.parseInt(likeNum));
            } catch (NumberFormatException e) {
                socialCount.setLikes(0);
            }
            nonceMessage.setSocialCount(socialCount);

            WriteFile.write(JsonMapper.buildNormalBinder().toJson(nonceMessage), "/Users/zeng.zhao/Desktop/weibo_comment-1.json");
        });

        String next = html.xpath("//div[@node-type='comment_loading']/@action-data").get();
        if (next == null) {
            next = html.xpath("//a[@action-type='click_more_comment']/@action-data").get();
        }
        if (StringUtils.isNotBlank(next)) {
            result.addRequest(new us.codecraft.webmagic.Request("https://weibo.com/aj/v6/comment/big?ajwvr=6&" + next + "&from=singleWeiBo"));
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        String url = "https://weibo.com/aj/v6/comment/big?ajwvr=6&id=4265760425546209&root_comment_max_id=4266417593528734&root_comment_max_id_type=1&root_comment_ext_param=&page=344&filter=hot&sum_comment_number=3492&filter_tips_before=1&from=singleWeiBo";
//        String url= "https://weibo.com/aj/v6/comment/big?ajwvr=6&id=4265760425546209&root_comment_max_id=138828954924041&root_comment_max_id_type=0&root_comment_ext_param=&page=2&filter=hot&sum_comment_number=15&filter_tips_before=0&from=singleWeiBo&__rnd=1535963379526";
        String content = "";
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
//            httpGet.addHeader("referer", "https://weibo.com/aj/v6/comment/big?ajwvr=6&id=4265760425546209&from=singleWeiBo");
            httpGet.addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
            httpGet.addHeader("cookie", "SINAGLOBAL=638387246136.2206.1527731616813; UOR=,,login.sina.com.cn; ALF=1567236156; SCF=AvXliNK2E1oyuXpLHOqFJwmipzSDe8bW4H38H0zcNHJ41J7l24emKUbQocUMeCoSFqQbauBYMoqwU6BVXi1SpPw.; SUHB=0BLoG_WX2Xrs7O; YF-Ugrow-G0=ea90f703b7694b74b62d38420b5273df; SUB=_2AkMs0CDjf8NxqwJRmPoVymrmbYV-zwjEieKajNE4JRMxHRl-yT9jqmEPtRB6B1AODMtYqlZ6kebgM1-Xs2z0vkxTZ3M_; SUBP=0033WrSXqPxfM72-Ws9jqgMF55529P9D9WhLKGPp6V4ym3h.LLIfeCQe; login_sid_t=9b41e9747458bca1aa3b77c1df91ad79; cross_origin_proto=SSL; YF-V5-G0=447063a9cae10ef9825e823f864999b0; _s_tentry=passport.weibo.com; wb_view_log=1440*9002; Apache=917146234034.4116.1535946711491; ULV=1535946711502:42:1:1:917146234034.4116.1535946711491:1535700165190; YF-Page-G0=ee5462a7ca7a278058fd1807a910bc74");
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                content = EntityUtils.toString(response.getEntity());
            }
        }
        System.out.println(content);
//        String content = Request.Get(url)
//                .execute()
//                .returnContent()
//                .asString();
//        Page page = new Page();
//        page.setRequest(new us.codecraft.webmagic.Request(url));
//        page.setUrl(new PlainText(url));
//        page.setRawText(content);
//        new WeiBoCommentPage().parse(page);
    }
}
