package com.lavector.collector.crawler.project.game.overwatch.blizzard.page;

import com.lavector.collector.crawler.base.PageParse;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 2017/10/24.
 *
 * @author zeng.zhao
 */
public class NewsListPage implements PageParse {
    @Override
    public boolean handleUrl(String url) {
        return url.equals("http://ow.blizzard.cn/article/news/");
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        result.addRequests(parseNewsListPage(page));
        return result;
    }

    @Override
    public String pageName() {
        return null;
    }

    private List<Request> parseNewsListPage (Page page) {
        Html html = page.getHtml();
        return html.xpath("//div[@class='flag_body flag-top blog-details']/a/@href").all().stream()
                .filter(url -> url.contains("article/news/"))
                .map(url -> {
                    url = url.replaceAll("\r\n", "");
                    if (!url.contains("blizzard.cn")) {
                        url = "http://ow.blizzard.cn" + url.trim();
                    }
                    return new Request(url.trim());
                }).collect(Collectors.toList());
    }

    public static void main (String[] args) throws Exception {
        String url = "http://ow.blizzard.cn/article/news/";
        String content = org.apache.http.client.fluent.Request.Get(url)
                .execute()
                .returnContent()
                .asString();
        Page page = new Page();
        page.setUrl(new Json(url));
        page.setRequest(new us.codecraft.webmagic.Request(url));
        page.setRawText(content);
        NewsListPage newsListPage = new NewsListPage();
        newsListPage.parse(page);
    }
}
