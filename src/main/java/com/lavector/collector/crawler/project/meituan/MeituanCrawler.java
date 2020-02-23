package com.lavector.collector.crawler.project.meituan;


import com.lavector.collector.crawler.base.BaseCrawler;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.MySpider;
import com.lavector.collector.crawler.project.meituan.com.pipeline.MeituanSearchPipeline;
import com.lavector.collector.crawler.project.meituan.com.pipeline.MeituanShopCommentPipeline;
import com.lavector.collector.crawler.project.meituan.downloader.MeituanDowloader;
import com.lavector.collector.crawler.project.weibo.weiboPerson.page.SougouWeixinDownloader;
import com.lavector.collector.crawler.util.ReadTextUtils;
import com.lavector.collector.crawler.util.UrlUtils;
import com.lavector.collector.entity.readData.SkuData;
import io.netty.handler.codec.http.HttpConstants;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.utils.HttpConstant;

import java.util.LinkedList;
import java.util.List;

public class MeituanCrawler extends BaseCrawler {


    public static void main(String[] args) {
        List<SkuData> skuDatas = ReadTextUtils.getSkuData("G:/text/meituan/shop.txt", ",");

        Spider spider = Spider.create(new MeituanProcesser(new CrawlerInfo()))
//                .addPipeline(new MeituanSearchPipeline("G:/text/meituan/data"));
                .addPipeline(new MeituanShopCommentPipeline("G:/text/meituan/data"));

        int i = 1;
        for (SkuData skudata : skuDatas) {
            skudata.setHeadWords(getShopCommentList());
            Request request = new Request();
            System.out.println("第" + i + "个");

//            String startUrl = "https://i.waimai.meituan.com/openh5/order/myuncompleteorder?wm_latitude=39908495&wm_longitude=116515664&wm_actual_latitude=39911618&wm_actual_longitude=116511890";
//            skudata.setUrl("https://i.waimai.meituan.com/openh5/search/poi?keyword=%E7%B2%A5");
//            String startUrl = handleBrandUrl(skudata.getBrand());
            String startUrl = handleShopCommentUrl(skudata.getBrand());

            if (!StringUtils.isEmpty(skudata.getUrl())) {
                skudata.setUrl(handleSearchShopUrl(skudata.getUrl()));
            }

            if (!"".equals(startUrl)) {
                request.setUrl(startUrl);
                request.putExtra("skuData", skudata);
                request.setMethod(HttpConstant.Method.POST);
                spider.addRequest(request);
            }
            i++;

            spider.thread(1);
//            Downloader downloader = new MeituanDowloader();
//            spider.setDownloader(downloader);
            spider.run();
        }

    }

    private static String handleBrandUrl(String brand) {
        if (StringUtils.isEmpty(brand)) {
            return brand;
        }
        String[] split = brand.split("，");
        return "https://i.waimai.meituan.com/openh5/order/myuncompleteorder?wm_latitude=" + split[0] + "&wm_longitude=" + split[1] +"&wm_actual_latitude=39911618&wm_actual_longitude=116511890";
    }


    private static String handleSearchShopUrl(String keyword) {
        return "https://i.waimai.meituan.com/openh5/search/poi?keyword=" + UrlUtils.encodeStr(keyword);
    }

    private static String handleShopCommentUrl(String shopId) {
        return "https://waimai.meituan.com/ajax/comment?wmpoiIdStr=" + shopId + "&offset=1";
    }

    public static List<String> getSearchShopList() {
        List<String> list = new LinkedList<>();
        list.add("关键词");
        list.add("店铺id");
        list.add("店名");
        list.add("月售");
        list.add("评分");
        list.add("人均");
        list.add("店铺url");
        return list;
    }

    public static List<String> getShopCommentList() {
        List<String> list = new LinkedList<>();
        list.add("店铺id");
        list.add("用户id");
        list.add("用户名");
        list.add("评论id");
        list.add("评论内容");
        list.add("评论时间");
        return list;
    }


    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {
        return null;
    }
}
