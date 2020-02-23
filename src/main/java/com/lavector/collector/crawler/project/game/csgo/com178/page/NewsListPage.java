package com.lavector.collector.crawler.project.game.csgo.com178.page;

import com.lavector.collector.crawler.base.PageParse;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/10/20.
 *
 * @author zeng.zhao
 */
public class NewsListPage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return url.equals("http://csgo.178.com/");
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        result.addRequests(parseHomePage(page));
        return result;
    }

    @Override
    public String pageName() {
        return null;
    }

    private List<Request> parseHomePage (Page page) {
        List<Request> requests = new ArrayList<>();
        Html html = page.getHtml();
        List<String> newsListUlrs = html
                .xpath("//div[@class='infonews fn-left']//li[@class='ui-repeater-item']/a/@href")
                .all();
        List<String> newsListUrlTags = html
                .xpath("//div[@class='infonews fn-left']//li[@class='ui-repeater-item']/a/i/text()")
                .all();
        for (int i = 0; i < newsListUlrs.size(); i++) {
            if (!newsListUrlTags.get(i).equals("视频")) {
                requests.add(new Request(newsListUlrs.get(i)));
            }
        }

        return requests;
    }

    public static void main (String[] args) throws Exception {
        String url = "http://csgo.178.com/";
        String content = org.apache.http.client.fluent.Request.Get(url)
                .execute()
                .returnContent()
                .asString();
        Page page = new Page();
        page.setRawText(content);
        page.setRequest(new Request(url));
        page.setUrl(new Json(url));
        NewsListPage homePage = new NewsListPage();
        homePage.parseHomePage(page);
    }
}
