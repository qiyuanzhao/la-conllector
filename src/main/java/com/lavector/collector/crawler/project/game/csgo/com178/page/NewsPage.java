package com.lavector.collector.crawler.project.game.csgo.com178.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import com.lavector.collector.def.Site;
import com.lavector.collector.entity.data.CategoryConstant;
import com.lavector.collector.entity.data.News;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.client.fluent.Request;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;

import java.text.ParseException;

/**
 * Created on 2017/10/20.
 *
 * @author zeng.zhao
 */
public class NewsPage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("http://csgo.178.com/\\d+/\\d+\\.html", url);
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
        String title = html.xpath("//div[@class='article']/div[@class='hd']/h3/text()").get();
        String author = html.xpath("//div[@class='article']/div[@class='hd']/div/span[@class='author']/text()")
                .get().trim();
        String originSource = html.xpath("//div[@class='article']/div[@class='hd']/div/span[@class='source']/text()")
                .get().trim();
        String timePublishStr = html.xpath("//div[@class='article']/div[@class='hd']/div/span[@class='time']/text()")
                .get();
        String content = html.xpath("//div[@class='article']/div[@class='bd']/allText()").get();
        news.setTitle(title);
        try {
            news.setTimePublish(DateUtils.parseDate(timePublishStr, "yyyy-MM-dd"));
        } catch (ParseException e) {
            throw new IllegalStateException("parse timePublish error : " + timePublishStr);
        }
        news.setAuthor(author);
        news.setOriginSource(originSource);
        news.setContent(content);
        news.setCategory(CategoryConstant.EGAME);
        news.setSite(Site.GAME178);
        news.setUrl(page.getUrl().get());
        news.setSource("178CSGO");
        return news;
    }

    public static void main (String[] args) throws Exception {
        String url = "http://csgo.178.com/201710/302479387667.html";
        String content = Request.Get(url)
                .execute()
                .returnContent()
                .asString();
        Page page = new Page();
        page.setUrl(new Json(url));
        page.setRequest(new us.codecraft.webmagic.Request(url));
        page.setRawText(content);
        NewsPage newsPage = new NewsPage();
        newsPage.parseNewsPage(page);
    }
}
