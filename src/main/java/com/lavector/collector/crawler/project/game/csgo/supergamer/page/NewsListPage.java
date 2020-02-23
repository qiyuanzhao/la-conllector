package com.lavector.collector.crawler.project.game.csgo.supergamer.page;

import com.lavector.collector.crawler.base.PageParse;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
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
        return url.equals("http://csgo.sgamer.com/news/list/1.html");
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
                url = "http://csgo.sgamer.com" + url;
            }
            requests.add(new Request(url).putExtra("title", title));
        });
        return requests;
    }
}
