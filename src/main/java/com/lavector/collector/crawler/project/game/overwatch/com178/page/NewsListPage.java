package com.lavector.collector.crawler.project.game.overwatch.com178.page;

import com.lavector.collector.crawler.base.PageParse;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/10/23.
 *
 * @author zeng.zhao
 */
public class NewsListPage implements PageParse {
    @Override
    public boolean handleUrl(String url) {
        return url.equals("http://ow.178.com/");
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
        List<Request> requests = new ArrayList<>();
        Html html = page.getHtml();
        List<Selectable> nodes = html.xpath("//div[@id='news-container']/div/div[@class='swiper-slide']").nodes();
        for (Selectable node : nodes) {
            List<Selectable> newsListNodes = node.xpath("//ul/li/a").nodes();
            for (Selectable newsListNode : newsListNodes) {
                String title = newsListNode.xpath("a/@title").get();
                String url = newsListNode.xpath("a/@href").get();
                if (!title.contains("[视频]") && !title.contains("[图集]")) {
                    System.out.println(title + "---" + url);
                    requests.add(new Request(url));
                }
            }
        }
        return requests;
    }

    public static void main (String[] args) throws Exception {
        String url = "http://ow.178.com/";
        String content = org.apache.http.client.fluent.Request.Get(url)
                .execute()
                .returnContent()
                .asString();
        Page page = new Page();
        page.setRawText(content);
        page.setRequest(new Request(url));
        page.setUrl(new Json(url));
        NewsListPage newsListPage = new NewsListPage();
        newsListPage.parse(page);
    }
}
