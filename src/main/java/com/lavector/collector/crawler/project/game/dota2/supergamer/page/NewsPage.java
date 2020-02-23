package com.lavector.collector.crawler.project.game.dota2.supergamer.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import com.lavector.collector.def.Site;
import com.lavector.collector.entity.data.CategoryConstant;
import com.lavector.collector.entity.data.News;
import org.apache.commons.lang3.time.DateUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;

import java.nio.charset.Charset;
import java.text.ParseException;

/**
 * Created on 2017/10/23.
 *
 * @author zeng.zhao
 */
public class NewsPage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("http://dota2.sgamer.com/news/\\d+/\\d+\\.html", url);
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
        String title = html.xpath("//div[@class='crumb-main']/ol/li[@class='active']/text()").get();
        String newsInfo = html.xpath("//div[@class='news-info']/text()").get().trim();
        String timePublish = new Json(newsInfo).regex("(\\d+-\\d+-\\d+\\ \\d+\\:\\d+)").get();
        String originSource = new Json(newsInfo).regex("来源：(.*)").get();
        String author = html.xpath("//div[@class='news-info']/span/text()").regex("作者：(.*)").get().trim();
        String content = html.xpath("//div[@class='news-text']/allText()").get().trim();
        news.setTitle(title);
        news.setContent(content);
        news.setAuthor(author);
        news.setOriginSource(originSource);
        try {
            news.setTimePublish(DateUtils.parseDate(timePublish, "yyyy-MM-dd HH:mm"));
        } catch (ParseException e) {
            throw new IllegalStateException("parse timePublish error : " + timePublish);
        }
        news.setUrl(page.getUrl().get());
        news.setCategory(CategoryConstant.EGAME);
        news.setSource("dota2超级玩家");
        news.setSite(Site.SGAMEAR);
        return news;
    }

    public static void main(String[] args) throws Exception {
        String url = "http://dota2.sgamer.com/news/201709/167835.html";
        String content = org.apache.http.client.fluent.Request.Get(url)
                .execute()
                .returnContent()
                .asString(Charset.forName("utf-8"));
        Page page = new Page();
        page.setRawText(content);
        page.setRequest(new Request(url));
        page.setUrl(new Json(url));
        NewsPage newsPage = new NewsPage();
        newsPage.parseNewsPage(page);
    }
}
