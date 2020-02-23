package com.lavector.collector.crawler.project.game.dota2.pcgames.page;

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

import java.nio.charset.Charset;
import java.text.ParseException;

/**
 * Created on 2017/10/20.
 *
 * @author zeng.zhao
 */
public class PCGamesNewsPage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("http://fight.pcgames.com.cn/\\d+/\\d+\\.html", url);
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
        String title = html.xpath("//div[@class='article']/h1/text()").get();
        String author = html.xpath("//div[@class='article-info']/span[@class='article-info-author']/text()")
                .regex("作者：(.*)").get();
        String originSource = html.xpath("//div[@class='article-info']/span[@class='article-info-source']/text()")
                .regex("来源：(.*)").get();
        String timePublish = html.xpath("//div[@class='article-info']/span[@class='article-info-date']/text()").get().trim();
        String content = html.xpath("//div[@class='article-content']/allText()").get().trim();
        news.setSite(Site.PCGAMES);
        news.setContent(content);
        news.setTitle(title);
        news.setAuthor(author);
        news.setOriginSource(originSource);
        try {
            news.setTimePublish(DateUtils.parseDate(timePublish, "yyyy-MM-dd HH:mm"));
        } catch (ParseException e) {
            throw new IllegalStateException("parse timePublish error: " + timePublish);
        }
        news.setUrl(page.getUrl().get());
        news.setCategory(CategoryConstant.EGAME);
        news.setSource("太平洋DOTA2专区");
        return news;
    }

    public static void main (String[] ars) throws Exception {
        String url = "http://fight.pcgames.com.cn/688/6881515.html";
        String content = Request.Get(url)
                .execute()
                .returnContent()
                .asString(Charset.forName("gbk"));
        Page page = new Page();
        page.setRawText(content);
        page.setRequest(new us.codecraft.webmagic.Request(url));
        page.setUrl(new Json(url));
        PCGamesNewsPage newsPage = new PCGamesNewsPage();
        newsPage.parse(page);
    }
}
