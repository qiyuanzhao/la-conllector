package com.lavector.collector.crawler.project.food.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import org.apache.commons.lang3.StringUtils;
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
public class ShopListByRegionPage implements PageParse {
    @Override
    public boolean handleUrl(String url) {
//        return RegexUtil.isMatch("http://www.dianping.com/search/category/\\d+/\\d+/g\\d+\\w\\d+.*", url);
        //行政区
        return RegexUtil.isMatch(".*www.dianping.com/search/keyword/\\d+/0_.*/r\\d+.*", url);
    }

    @Override
    public Result parse(Page page) {
        return parsePageBySearch(page);
    }

    @Override
    public String pageName() {
        return null;
    }


    /**
     * 指定类别搜索
     * @return Result
     */
    private Result parsePageByCategory(Page page) {
        Result result = Result.get();
        Html html = page.getHtml();
        String category = page.getRequest().getExtra("category").toString();
        List<Request> requests = html.xpath("//div[@class='pic']/a/@href").all().stream()
                .map(url -> {
                    Request request = new Request(url);
                    request.putExtra("category", category);
                    return request;
                })
                .collect(Collectors.toList());
        result.addRequests(requests);
        String nextUrl = html.xpath("//a[@class='next']/@href").get();
        if (StringUtils.isNotBlank(nextUrl)) {
            result.addRequest(new Request(nextUrl).putExtra("category", category));
        }
        return result;
    }

    /**
     * 默认搜索 获取推荐菜列表
     * @return Result
     */
    private Result parsePageBySearch(Page page) {
        Result result = Result.get();
        Html html = page.getHtml();
        String dishName = page.getRequest().getExtra("dishName").toString();
        List<Request> requests = html.xpath("//div[@class='txt']/div[@class='tit']/a/@href").all()
                .stream()
                .map(url -> new Request(url + "/dishlist").putExtra("dishName", dishName))
                .collect(Collectors.toList());
        result.addRequests(requests);

        String nextPage = html.xpath("//div[@class='page']/a[@class='next']/@href").get();
        if (StringUtils.isNotBlank(nextPage)) {
            result.addRequest(new Request(nextPage).putExtra("dishName", dishName));
        }
        return result;
    }
}
