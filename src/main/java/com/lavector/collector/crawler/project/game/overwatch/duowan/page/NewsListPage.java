package com.lavector.collector.crawler.project.game.overwatch.duowan.page;

import com.lavector.collector.crawler.base.PageParse;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 2017/10/25.
 *
 * @author zeng.zhao
 */
public class NewsListPage implements PageParse {
    @Override
    public boolean handleUrl(String url) {
        return url.equals("http://ow.duowan.com/tag/289238611958.html");
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
        return html.xpath("//div[@class='ch-list']/ul[@class='ch-list-sec']/a/@href").all().stream()
                .map(url -> {
                    if (!url.contains("duowan.com")) {
                        url = "http://ow.duowan.com" + url;
                    }
                    return new Request(url);
                }).collect(Collectors.toList());
    }
}
