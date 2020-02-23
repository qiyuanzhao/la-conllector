package com.lavector.collector.crawler.project.food.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 2017/11/6.
 *
 * @author zeng.zhao
 */
public class ShopListByDefaultPage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("http://www.dianping.com/search/category/\\d+/\\d+/g\\d+$", url);
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        Html html = page.getHtml();// 按行政区抓取
        String category = page.getRequest().getExtra("category").toString();
        List<Request> requests = html.xpath("//div[@id='region-nav']/a/@href").all().stream()
                .map(url -> {
                    Request request = new Request(url);
                    request.putExtra("category", category);
                    return request;
                })
                .collect(Collectors.toList());
        result.addRequests(requests);
        return result;
    }

    @Override
    public String pageName() {
        return null;
    }
}
