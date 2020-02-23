package com.lavector.collector.crawler.project.article.zaker.page;

import com.lavector.collector.crawler.base.PageParse;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 2018/1/2.
 *
 * @author zeng.zhao
 */
public class ZakerSearchListPage implements PageParse {
    @Override
    public boolean handleUrl(String url) {
        return url.contains("www.myzaker.com/news/search_new.php");
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
        List<Request> requests = html.xpath("//div[@class='article flex-1']/h2/a/@href").all().stream().map(url -> {
            if (!url.contains("http:")) {
                url = "http:" + url;
            }
            Request request = new Request(url);
            request.putExtra("brand", brand);
            return request;
        }).collect(Collectors.toList());
        result.addRequests(requests);
        return result;
    }
}
