package com.lavector.collector.crawler.project.game.overwatch.blizzard.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import com.lavector.collector.entity.data.CategoryConstant;
import com.lavector.collector.entity.data.News;
import org.apache.commons.lang3.time.DateUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;

import java.text.ParseException;

/**
 * Created on 2017/10/24.
 *
 * @author zeng.zhao
 */
public class NewsPage implements PageParse {
    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("http://ow.blizzard.cn/article/news/\\d+", url);
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
        String title = html.xpath("//title/text()").get();
        String timePublish = html.xpath("//div[@class='article-meta']/span/text()").regex("\\d+-\\d+-\\d+").get();
        String content = html.xpath("//div[@class='blog-content']/div/allText()").get();
        news.setTitle(title);
        try {
            news.setTimePublish(DateUtils.parseDate(timePublish, "yyyy-MM-dd"));
        } catch (ParseException e) {
            throw new IllegalStateException("parse timePublish error : " + timePublish);
        }
        news.setContent(content);
        news.setSource("守望先锋官网");
        news.setCategory(CategoryConstant.EGAME);
        news.setUrl(page.getUrl().get());
        return news;
    }

    public static void main (String[] args) throws Exception {
        String url = "http://ow.blizzard.cn/article/news/730";
        String content = org.apache.http.client.fluent.Request.Get(url)
                .execute()
                .returnContent()
                .asString();
        Page page = new Page();
        page.setUrl(new Json(url));
        page.setRequest(new us.codecraft.webmagic.Request(url));
        page.setRawText(content);
        NewsPage newsPage = new NewsPage();
        newsPage.parse(page);
    }
}
