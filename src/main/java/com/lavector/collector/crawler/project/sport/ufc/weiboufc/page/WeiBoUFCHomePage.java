package com.lavector.collector.crawler.project.sport.ufc.weiboufc.page;

import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.def.Site;
import com.lavector.collector.entity.data.CategoryConstant;
import com.lavector.collector.entity.data.News;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.client.fluent.Request;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/10/19.
 *
 * @author zeng.zhao
 */
public class WeiBoUFCHomePage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return url.equals("http://weibo.com/ufc?profile_ftype=1&is_all=1");
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        List<News> newsList = parseHomePage(page);
        newsList.forEach(result::addData);
        return result;
    }

    @Override
    public String pageName() {
        return null;
    }

    private List<News> parseHomePage (Page page) {
        List<News> newsList = new ArrayList<>();
        Html html = page.getHtml();
        String messageInfo = getMessageInfo(html);
        if (StringUtils.isBlank(messageInfo)) {
            return null;
        }
        Html messageHtml = new Html(messageInfo);
        List<Selectable> nodes = messageHtml.xpath("//div[@class='WB_detail']").nodes();
        for (Selectable node : nodes) {
            News news = new News();
            String author = node.xpath("//div[@class='WB_info']/a[1]/text()").get();
            String timePublish = node.xpath("//div[@class='WB_from S_txt2']/a[1]/@title").get();
            String originSource = node.xpath("//div[@class='WB_from S_txt2']/a[2]/text()").get();
            String content = node.xpath("//div[@class='WB_text W_f14']/allText()").get();
//            String timePublish = null;
//            if (timePublishInfo.contains("年")) {
//                String timePublishReplace1 = timePublishInfo.replace("年", "-");
//                String timePublishReplace2 = timePublishReplace1.replace("月", "-");
//                timePublish = timePublishReplace2.replace("日", "");
//            } else {
//                Calendar calendar = Calendar.getInstance();
//                String timePublishReplace1 = timePublishInfo.replace("月", "-");
//                timePublish = calendar.get(Calendar.YEAR) + "-" + timePublishReplace1.replace("日", "");
//            }
            news.setAuthor(author);
            news.setOriginSource(originSource);
            try {
                news.setTimePublish(DateUtils.parseDate(timePublish, "yyyy-MM-dd HH:mm"));
            } catch (ParseException e) {
                throw new IllegalStateException("parse timePublish error: " + timePublish);
            }
            news.setContent(content);
            news.setUrl(page.getUrl().get());
            news.setCategory(CategoryConstant.SPORT);
            news.setSource("微博UFC官微");
            news.setSite(Site.WEIBO);
            newsList.add(news);
        }
        return newsList;
    }

    private String getMessageInfo (Html html) {
        String messageHtml = null;
        List<String> scripts = html.xpath("//script").all();
        for (String script : scripts) {
            if (script.contains("正在加载中，请稍候...")) {
                String messageJson = script.substring(script.indexOf("(") + 1, script.lastIndexOf(")"));
                messageHtml = JsonPath.read(messageJson, "$.html");
            }
        }
        return messageHtml;
    }

    public static void main (String[] args) throws Exception {
        String url = "http://weibo.com/ufc?profile_ftype=1&is_all=1#_0";
        String content = Request.Get(url)
                .addHeader("cookie", "SUB=_2AkMutMcZf8NxqwJRmP4TyG_lZIlzyAHEieKY6DbCJRMxHRl-yT83qkIrtRApj2jQyjYXa7K1fhefC2Q_lZ631Q..")
                .execute()
                .returnContent()
                .asString();
        Page page = new Page();
        page.setUrl(new Json(url));
        page.setRequest(new us.codecraft.webmagic.Request(url));
        page.setRawText(content);
        WeiBoUFCHomePage homePage = new WeiBoUFCHomePage();
        homePage.parseHomePage(page);
    }
}
