package com.lavector.collector.crawler.project.food.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.edu.WriteFile;
import com.lavector.collector.crawler.project.food.DianpingCrawler;
import com.lavector.collector.crawler.util.RegexUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created on 2017/11/7.
 *
 * @author zeng.zhao
 */
public class DishListPage implements PageParse {

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("http://www.dianping.com/shop/\\d+/dishlist.*", url);
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
//        Request dishCommentByName = getDishCommentByName(page);
//        if (dishCommentByName != null) {
//            result.addRequest(dishCommentByName);
//        }

        Request dishMessage = getDishMessage(page);
        if (dishMessage != null) {
            result.addRequest(dishMessage);
        }
        return result;
    }

    @Override
    public String pageName() {
        return null;
    }

    private String basePath = "/Users/zeng.zhao/Desktop/dianping/";


    /**
     *  根据菜品获取评论url
     * @return url
     */
    private Request getDishCommentByName(Page page) {
        Html html = page.getHtml();
        String targetDishName = page.getRequest().getExtra("dishName").toString();
        List<Selectable> nodes = html.xpath("//a[@class='shop-food-item']").nodes();
        for (Selectable node : nodes) {
            String dishName = node.xpath("//div[@class='shop-food-con']/div[@class='shop-food-name']/text()").get();
            if (StringUtils.contains(dishName, targetDishName)) {
                String shopId = page.getUrl().regex("/shop/(\\d+)/dish").get();
                String dishId = node.xpath("//a[@class='shop-food-item']/@href").regex("dish(\\d+)").get();
                String dishCommentUrl = "http://www.dianping.com/shop/" + shopId +
                        "/review_search_" + dishName;
                Request request = new Request(dishCommentUrl);
                request.putExtra("dishId", dishId).putExtra("shopId", shopId);
                return request;
            }
        }
        String nextUrl = html.xpath("//a[@class='next']/@href").get();
        if (StringUtils.isNotBlank(nextUrl)) {
            if (!nextUrl.contains("http")) {
                nextUrl = "http://www.dianping.com" + nextUrl;
            }
            return new Request(nextUrl).putExtra("dishName", targetDishName);
        }
        return null;
    }

    /**
     *  抓取菜品信息
     * @return Request
     */
    private Request getDishMessage(Page page) {
        Html html = page.getHtml();
        String targetDishName = page.getRequest().getExtra("dishName").toString();
        List<Selectable> nodes = html.xpath("//a[@class='shop-food-item']").nodes();
        List<Dish> dishs = new ArrayList<>();
        for (Selectable node : nodes) {
            String dishName = node.xpath("//div[@class='shop-food-con']/div[@class='shop-food-name']/text()").get();
            String recommendCount = node.xpath("//div[@class='shop-food-con']/div[@class='recommend-count']/text()")
                    .regex("\\d+").get();
            String dishUrl = node.xpath("//a/@href").get();
            if (!dishUrl.contains("http")) {
                dishUrl = "http:" + dishUrl;
            }
            String dishImage = node.xpath("//div[@class='shop-food-img']/img/@src").get();
            Dish dish = new Dish();
            dish.dishName = dishName;
            dish.recommendCount = recommendCount;
            dish.url = dishUrl;
            dish.image = dishImage;
            if (dishName.contains(targetDishName)) {
                dishs.add(dish);
            }
        }
        String shopName = html.xpath("//meta[@itemprop='name']/@content").get();
        String city = html.xpath("//meta[@name='location']/@content").regex("province=(.*);city").get();
        if (!DianpingCrawler.targetCitys.contains(city)) {
            city = html.xpath("//meta[@name='location']/@content").regex("city=(.*);").get();
        }

        if (CollectionUtils.isNotEmpty(dishs)) {
            writeFile(city, shopName, dishs);

            System.out.println("写入成功 ！" + shopName);
        }


        String nextUrl = html.xpath("//a[@class='next']/@href").get();
        if (StringUtils.isNotBlank(nextUrl)) {
            if (!nextUrl.contains("http")) {
                nextUrl = "http://www.dianping.com" + nextUrl;
            }
            return new Request(nextUrl).putExtra("dishName", targetDishName);
        }
        return null;
    }

    private  void writeFile(String targetCity, String shopName, List<Dish> dishs) {
        for (Dish dish : dishs) {
            String content = dish.dishName + "," +
                    dish.recommendCount +
                    "," +
                    dish.image +
                    "," +
                    dish.url +
                    ",";
            WriteFile.write(content, basePath + targetCity + "/" + shopName + ".csv");
        }
    }

    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    class Dish {
        String dishName;
        String recommendCount;
        String url;
        String image;
    }
}
