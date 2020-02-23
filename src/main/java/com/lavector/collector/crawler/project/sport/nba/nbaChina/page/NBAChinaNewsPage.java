package com.lavector.collector.crawler.project.sport.nba.nbaChina.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import com.lavector.collector.def.Site;
import com.lavector.collector.entity.data.CategoryConstant;
import com.lavector.collector.entity.data.News;
import org.apache.http.client.fluent.Request;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created on 2017/10/17.
 *
 * @author zeng.zhao
 */
public class NBAChinaNewsPage implements PageParse {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("http://nbachina.qq.com/a/\\d+/\\d+\\.htm", url);
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        result.addData(parseNewsPage(page));
        return result;
    }

    @Override
    public String pageName() {
        return null;
    }

    private News parseNewsPage (Page page) {
        News news = new News();
        Html html = page.getHtml();
        String title = html.xpath("//div[@class='hd']/h1/text()").get();
        String content = html.xpath("//div[@bossZone='content']/allText()").get();
        String originSource = html.xpath("//div[@class='hd']/div/div/span[@bossZone='jgname']/text()").get();
        String author = html.xpath("//div[@id='QQeditor']/text()").regex("：(\\w+)\\]").get();
        String timePublishStr = html.xpath("//div[@class='hd']/div/div/span[@class='article-time']/text()").get();
        news.setCategory(CategoryConstant.SPORT);
        news.setSite(Site.NBA_CHINA);
        news.setSource("NBA中国");
        news.setUrl(page.getUrl().get());
        news.setTimePublish(parseTimePublish(timePublishStr));
        news.setTitle(title);
        news.setAuthor(author);
        news.setContent(content);
        news.setOriginSource(originSource);
        return news;
    }

    private Date parseTimePublish (String timePublishStr) {
        try {
            return simpleDateFormat.parse(timePublishStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException("parse timePublish error: " + timePublishStr);
        }
    }

    public static void main (String[] args) throws Exception {
        String url = "http://nbachina.qq.com/a/20171017/015784.htm";
        String content = Request.Get(url).execute().returnContent().asString();
        Page page = new Page();
        page.setRawText(content);
        page.setRequest(new us.codecraft.webmagic.Request(url));
        page.setUrl(new Json(url));
        NBAChinaNewsPage newsPage = new NBAChinaNewsPage();
        newsPage.parse(page);
    }
}
