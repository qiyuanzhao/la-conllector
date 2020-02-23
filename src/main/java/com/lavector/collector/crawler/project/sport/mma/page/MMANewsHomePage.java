package com.lavector.collector.crawler.project.sport.mma.page;

import com.lavector.collector.crawler.base.PageParse;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 2017/10/18.
 *
 * @author zeng.zhao
 */
public class MMANewsHomePage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return url.equals("http://www.vs.cm/");
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        result.addRequests(parseNewsHomePage(page));
        return result;
    }

    @Override
    public String pageName() {
        return null;
    }

    private List<Request> parseNewsHomePage (Page page) {
        Html html = page.getHtml();
        return html.xpath("//a[@class='news-title']/@href").all().stream().map((String url) ->
                new Request("http://www.vs.cm" + url)
        ).collect(Collectors.toList());
    }
}
