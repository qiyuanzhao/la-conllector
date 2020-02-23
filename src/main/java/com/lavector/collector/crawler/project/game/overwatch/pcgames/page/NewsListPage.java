package com.lavector.collector.crawler.project.game.overwatch.pcgames.page;

import com.lavector.collector.crawler.base.PageParse;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 2017/10/23.
 *
 * @author zeng.zhao
 */
public class NewsListPage implements PageParse {
    @Override
    public boolean handleUrl(String url) {
        return url.equals("http://wangyou.pcgames.com.cn/ow/");
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
        return html.xpath("//div[@class='media-body-title']/h3/a/@href").all().stream()
                .map(Request::new).collect(Collectors.toList());
    }
}
