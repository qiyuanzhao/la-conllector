package com.lavector.collector.crawler.project.game.dota2.supergamer.page;

import com.lavector.collector.crawler.base.PageParse;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;

import java.nio.charset.Charset;
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
        return url.equals("http://dota2.sgamer.com/");
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
        List<Selectable> list = html.xpath("//div[@class='col-xs-8 col-md-8 col-lg-8 hnew-list']/b/a").nodes();
        list.forEach(node -> {
            String url = node.xpath("a/@href").get();
            String title = node.xpath("a/text()").get();
            if (!url.contains("sgamer.com")) {
                url = "http://dota2.sgamer.com" + url;
            }
            requests.add(new Request(url).putExtra("title", title));
        });
        return requests;
    }

    public static void main(String[] args) throws Exception {
        String url = "http://dota2.sgamer.com/";
        String content = org.apache.http.client.fluent.Request.Get(url)
                .execute()
                .returnContent()
                .asString(Charset.forName("utf-8"));
        Page page = new Page();
        page.setRawText(content);
        page.setRequest(new Request(url));
        page.setUrl(new Json(url));
        NewsListPage newsListPage = new NewsListPage();
        newsListPage.parseNewsListPage(page);
    }
}
