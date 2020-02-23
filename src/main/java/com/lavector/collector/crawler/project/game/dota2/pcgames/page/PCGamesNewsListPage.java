package com.lavector.collector.crawler.project.game.dota2.pcgames.page;

import com.lavector.collector.crawler.base.PageParse;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 2017/10/20.
 *
 * @author zeng.zhao
 */
public class PCGamesNewsListPage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return url.equals("http://fight.pcgames.com.cn/dota2/news/");
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
        return html.xpath("//div[@class='media-body-content']/a/@href").all().stream().map(url ->
            new Request(url.replace("\r\n", ""))
        ).collect(Collectors.toList());
    }

    public static void main (String[] args) throws Exception {
        String url = "http://fight.pcgames.com.cn/dota2/news/";
        String content = org.apache.http.client.fluent.Request.Get(url)
                .execute()
                .returnContent()
                .asString();
        Page page = new Page();
        page.setUrl(new Json(url));
        page.setRequest(new Request(url));
        page.setRawText(content);
        PCGamesNewsListPage newsListPage = new PCGamesNewsListPage();
        newsListPage.parseNewsListPage(page);
    }
}
