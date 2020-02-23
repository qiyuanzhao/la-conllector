package com.lavector.collector.crawler.project.game.ooqiu.page;

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
import us.codecraft.webmagic.selector.Selectable;

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
        return RegexUtil.isMatch("http://fight.ooqiu.com/a/\\d+/\\d+\\.html", url);
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
        String title = html.xpath("//div[@id='content']/h2[@class='arc-tit']/text()").get();
        Selectable sourceInfo = html.xpath("//div[@id='content']/p[@class='soucre']/text()");
        String timePublish = sourceInfo.regex("(\\d+-\\d+-\\d+\\ \\d+\\:\\d+)").get();
        String originSource = sourceInfo.regex("来源：(.*)作者").get();
        String author = sourceInfo.regex("作者：(.*)").get();
        String content = html.xpath("//div[@id='text']/allText()").get();
        news.setTitle(title);
        news.setOriginSource(originSource);
        news.setAuthor(author);
        news.setContent(content);
        try {
            news.setTimePublish(DateUtils.parseDate(timePublish, "yyyy-MM-dd HH:mm"));
        } catch (ParseException e) {
            throw new IllegalStateException("parse timePublish error : " + timePublish);
        }
        news.setCategory(CategoryConstant.EGAME);
        news.setUrl(page.getUrl().get());
        news.setSource("电竞世界");
        news.setSite(Site.OOQIU);
        return news;
    }

    public static void main (String[] args) throws Exception {
        String url = "http://fight.ooqiu.com/a/201710/393622.html";
        String content = org.apache.http.client.fluent.Request.Get(url)
                .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36")
                .execute()
                .returnContent()
                .asString(Charset.forName("gbk"));
        Page page = new Page();
        page.setRawText(content);
        page.setRequest(new Request(url));
        page.setUrl(new Json(url));
        NewsPage newsPage = new NewsPage();
        newsPage.parse(page);
    }
}
