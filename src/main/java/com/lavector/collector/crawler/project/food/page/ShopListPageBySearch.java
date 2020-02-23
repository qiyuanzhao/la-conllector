package com.lavector.collector.crawler.project.food.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 2017/12/28.
 *
 * @author zeng.zhao
 */
public class ShopListPageBySearch implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        //默认搜索
        return RegexUtil.isMatch(".*www.dianping.com/search/keyword/\\d+/0_.*", url);
    }

    @Override
    public String pageName() {
        return null;
    }


    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        result.addRequests(getShopDishList(page));
//        result.addRequests(getShopComment(page));
        return result;
    }

    /**
     *  获取店铺评论url
     * @return urls
     */
    private List<Request> getShopComment(Page page) {
        Html html = page.getHtml();
        List<String> shopLinks = html.xpath("//div[@class='txt']/div[@class='tit']/a/@href").all();
        return shopLinks.stream().map(url -> new Request(url + "/review_all")).collect(Collectors.toList());
    }

    /**
     *  获取店铺推荐菜url(按行政区)
     * @return urls
     */
    private List<Request> getShopDishList(Page page) {
        String dishName = page.getRequest().getExtra("dishName").toString();
        Html html = page.getHtml();
        //按行政区
        return html.xpath("//div[@id='region-nav']/a/@href").all().stream()
                .map(url -> {
                    Request request = new Request(url);
                    request.putExtra("dishName", dishName);
                    return request;
                })
                .collect(Collectors.toList());
    }

}
