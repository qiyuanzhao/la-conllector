package com.lavector.collector.crawler.project.article.huxiu.page;

import com.lavector.collector.crawler.base.PageParse;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 2017/12/26.
 *
 * @author zeng.zhao
 */
public class SearchListPage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return url.contains("www.huxiu.com/search.html");
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        Html html = page.getHtml();
        String brand = page.getRequest().getExtra("brand").toString();
        List<Request> requests = html.xpath("//div[@class='search-list-warp']/ul/li/h2/a/@href").all().stream()
                .map(url -> {
                    if (!url.contains("http://www")) {
                        url = "https://www.huxiu.com" + url;
                    }
                    Request request = new Request(url);
                    request.putExtra("brand", brand);
                    return request;
                }).collect(Collectors.toList());
        result.addRequests(requests);
        html.xpath("//nav[@class='page-nav']ul/li").nodes().stream().forEach(node -> {
            if (node.xpath("//i[@class='icon icon-gt']").get() != null) {
                String nextUrl = node.xpath("//a@href").get();
                if (!nextUrl.contains("http://www")) {
                    nextUrl = "https://www.huxiu.com" + nextUrl;
                }
                Request request = new Request(nextUrl);
                request.putExtra("brand", brand);
                result.addRequest(request);
            }
        });
        return result;
    }
}
