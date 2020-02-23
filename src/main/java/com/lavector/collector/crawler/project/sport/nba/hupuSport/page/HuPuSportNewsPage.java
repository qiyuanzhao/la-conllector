package com.lavector.collector.crawler.project.sport.nba.hupuSport.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import com.lavector.collector.entity.data.CategoryConstant;
import com.lavector.collector.entity.data.News;
import org.apache.http.client.fluent.Request;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created on 2017/10/16.
 *
 * @author zeng.zhao
 */
public class HuPuSportNewsPage implements PageParse {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("https://voice.hupu.com/nba/\\d+\\.html", url);
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        result.addData(parseNews(page));
        return result;
    }

    @Override
    public String pageName() {
        return null;
    }

    private News parseNews (Page page) {
        News news = new News();
        Html html = page.getHtml();
        List<String> contents = html.xpath("//div[@class='artical-content']/div[@class='artical-content-read']/div[2]/p/text()").all();
        String content = contents.stream().reduce((s1, s2) ->  s1 + s2).get();
        String title = html.xpath("//div[@class='artical-title']/h1[@class='headline']/text()").get();
        Selectable articleInfo = html.xpath("//div[@class='artical-title']/div[@class='artical-info']");
        String originSource = articleInfo.xpath("span/span/a/text()").get();
        String timePublishStr = articleInfo.xpath("span/a/span/text()").get().trim();
        String author = html.xpath("//span[@id='editor_baidu']/text()").regex("：(\\W+)\\)").get();
        news.setAuthor(author);
        news.setContent(content);
        news.setOriginSource(originSource);
        news.setTitle(title);
        news.setUrl(page.getUrl().get());
        news.setTimePublish(this.dateFormat(timePublishStr));
        news.setSource("虎扑");
        news.setCategory(CategoryConstant.SPORT);
        return news;
    }

    private Date dateFormat (String source) {
        try {
            return simpleDateFormat.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("date format error!" + source);
    }

    public static void main (String[] args) throws Exception {
        String url = "https://voice.hupu.com/nba/2217691.html";
        String content = Request.Get(url).execute().returnContent().asString();
        Page page = new Page();
        page.setRawText(content);
        page.setUrl(new Json(url));
        page.setRequest(new us.codecraft.webmagic.Request(url));
        HuPuSportNewsPage newsPage = new HuPuSportNewsPage();
        newsPage.parse(page);
    }
}
