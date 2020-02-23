package com.lavector.collector.crawler.project.game.overwatch.duowan.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import com.lavector.collector.def.Site;
import com.lavector.collector.entity.data.CategoryConstant;
import com.lavector.collector.entity.data.News;
import org.apache.commons.lang3.time.DateUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;

import java.nio.charset.Charset;
import java.text.ParseException;

/**
 * Created on 2017/10/25.
 *
 * @author zeng.zhao
 */
public class NewsPage implements PageParse {
    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("http://ow.duowan.com/\\d+/\\d+\\.html", url);
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

    private News parseNewsPage(Page page) {
        News news = new News();
        Html html = page.getHtml();
        String title = html.xpath("//div[@class='ZQ-page ZQ-page--article']/h1/text()").get();
        String timePublish = html.xpath("//div[@class='ZQ-page ZQ-page--article']/address/span[1]/text()").get().trim();
        String originSource = html.xpath("//div[@class='ZQ-page ZQ-page--article']/address/span[3]/text()").regex("来源：(.*)").get();
        String author = html.xpath("//div[@class='ZQ-page ZQ-page--article']/address/span[4]/text()").regex("作者：(.*)").get();
        String content = html.xpath("//div[@id='text']/allText()").get();
        news.setTitle(title);
        try {
            news.setTimePublish(DateUtils.parseDate(timePublish, "yyyy-MM-dd HH:mm:ss"));
        } catch (ParseException e) {
            throw new IllegalStateException("parse timePublish error : " + timePublish);
        }
        news.setOriginSource(originSource);
        news.setAuthor(author);
        news.setContent(content);
        news.setUrl(page.getUrl().get());
        news.setCategory(CategoryConstant.EGAME);
        news.setSource("多玩守望先锋");
        news.setSite(Site.DUOWAN);
        return news;
    }

    public static void main(String[] args) throws Exception {
        String url = "http://ow.duowan.com/1710/372852292982.html";
        String content = org.apache.http.client.fluent.Request.Get(url)
                .execute()
                .returnContent()
                .asString(Charset.forName("utf-8"));
        Page page = new Page();
        page.setUrl(new Json(url));
        page.setRequest(new us.codecraft.webmagic.Request(url));
        page.setRawText(content);
        NewsPage newsPage = new NewsPage();
        newsPage.parse(page);
    }
}
