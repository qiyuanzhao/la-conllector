package com.lavector.collector.crawler.project.game.overwatch.com178.page;

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
 * Created on 2017/10/23.
 *
 * @author zeng.zhao
 */
public class NewsPage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("http://ow.178.com/\\d+/\\d+\\.html", url);
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
        String title = html.xpath("//div[@class='arc-text']/h1/text()").get();
        String author = html.xpath("//div[@class='arc-text']/h2/span[1]/text()").regex("作者：(.*)").get();
        String originSource = html.xpath("//div[@class='arc-text']/h2/span[2]/text()").regex("来源：(.*)").get();
        String timePublish = html.xpath("//div[@class='arc-text']/h2/span[3]/text()").regex("发布时间：(.*)").get();
        String content = html.xpath("//div[@class='txt']/allText()").get();
        news.setTitle(title);
        news.setAuthor(author);
        news.setOriginSource(originSource);
        news.setContent(content);
        try {
            news.setTimePublish(DateUtils.parseDate(timePublish, "yyyy-MM-dd HH:mm:ss"));
        } catch (ParseException e) {
            throw new IllegalStateException("parse timePublish error : " + timePublish);
        }
        news.setUrl(page.getUrl().get());
        news.setCategory(CategoryConstant.EGAME);
        news.setSource("178守望先锋专区");
        news.setSite(Site.GAME178);
        return news;
    }

    public static void main (String[] args) throws Exception {
        String url = "http://ow.178.com/201710/302665115708.html";
        String content = Request.Get(url)
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
