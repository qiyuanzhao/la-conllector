package com.lavector.collector.crawler.project.game.dota2.com178.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import com.lavector.collector.def.Site;
import com.lavector.collector.entity.data.CategoryConstant;
import com.lavector.collector.entity.data.News;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
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
        return RegexUtil.isMatch("http://dota2.178.com/\\d+/\\d+\\.html", url);
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        News news = parseNewsPage(page);
        if (news != null) {
            result.addData(news);
        }
        return result;
    }

    @Override
    public String pageName() {
        return null;
    }

    private News parseNewsPage(Page page) {
        News news = new News();
        Html html = page.getHtml();
        String title = html.xpath("//div[@class='article']/div[@class='hd']/h3/text()").get();
        if (StringUtils.isBlank(title)) {
            return null;
        }
        String author = html.xpath("//div[@class='article']/div[@class='hd']/div/span[@class='author']/text()")
                .get();
        String originSource = html.xpath("//div[@class='article']/div[@class='hd']/div/span[@class='source']/text()")
                .get();
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
        news.setSource("178dota");
        return news;
    }

    public static void main (String[] args) throws Exception {
        String url = "http://dota2.178.com/201710/302576240310.html";
        String content = org.apache.http.client.fluent.Request.Get(url)
                .execute()
                .returnContent()
                .asString();
        Page page = new Page();
        page.setRawText(content);
        page.setRequest(new Request(url));
        page.setUrl(new Json(url));
        NewsPage newsPage = new NewsPage();
        newsPage.parseNewsPage(page);
    }
}
