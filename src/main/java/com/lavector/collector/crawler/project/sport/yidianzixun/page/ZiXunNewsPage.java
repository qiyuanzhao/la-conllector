package com.lavector.collector.crawler.project.sport.yidianzixun.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.base.RequestExtraKey;
import com.lavector.collector.crawler.util.RegexUtil;
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
public class ZiXunNewsPage implements PageParse {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("http://www.yidianzixun.com/mp/content\\?id=\\d+", url);
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
        String timePublish = page.getRequest().getExtras().get(RequestExtraKey.KEY_BEGIN_DATE).toString();
        String title = html.xpath("//div[@class='left-wrapper']/h2/text()").get();
        String content = html.xpath("//div[@class='imedia-article']/allText()").get();
        String author = html.xpath("//a[@class='doc-source']/text()").get();
        String originSource = html.xpath("//span[@class='wm-copyright']/text()").get();
        news.setOriginSource(originSource);
        news.setContent(content);
        news.setAuthor(author);
        news.setTitle(title);
        news.setTimePublish(parseTimePublish(timePublish));
        news.setUrl(page.getUrl().get());
        news.setSource("一点资讯");
        news.setCategory(CategoryConstant.SPORT);
        return news;
    }

    public static Date parseTimePublish (String timePublishStr) {
        try {
            return simpleDateFormat.parse(timePublishStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException("parse timePublish error: " + timePublishStr);
        }
    }

    public static void main (String[] args) throws Exception {
        String url = "http://www.yidianzixun.com/article/0HU7skS0";
        String content = Request.Get(url)
                .execute()
                .returnContent()
                .asString();
        Page page = new Page();
        page.setRawText(content);
        page.setRequest(new us.codecraft.webmagic.Request(url).putExtra(RequestExtraKey.KEY_BEGIN_DATE, new Date()));
        page.setUrl(new Json(url));
        ZiXunNewsPage newsPage = new ZiXunNewsPage();
        newsPage.parse(page);
    }
}
