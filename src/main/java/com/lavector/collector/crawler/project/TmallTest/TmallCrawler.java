package com.lavector.collector.crawler.project.TmallTest;

import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.project.tmall.TmallPageProcessor;
import com.lavector.collector.crawler.project.weibo.weiboPerson.page.SougouWeixinDownloader;
import com.lavector.collector.crawler.project.zhihu.ZhihuProcesser;
import com.lavector.collector.crawler.project.zhihu.pipepine.AnswerPopeline;
import com.lavector.collector.crawler.util.ReadTextUtils;
import com.lavector.collector.crawler.util.RegexUtil;
import com.lavector.collector.crawler.util.UrlUtils;
import com.lavector.collector.entity.readData.SkuData;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

import java.util.List;

/**
 * Created by qyz on 2019/7/24.
 */
public class TmallCrawler {

    private static String listUrl = "https://list.tmall.com/search_product.htm?q=";
//    private static String listUrl = "https://list.tmall.com/search_product.htm?spm=a220m.1000858.1000724.4.58194aedvfz9cw&cat=51220040&q=%D3%A4%B6%F9&sort=d&style=g&from=sn_1_cat-qp&industryCatId=50025137&smAreaId=110100#J_Filter";

    public static void main(String[] args) {
        List<SkuData> skuDatas = ReadTextUtils.getSkuData("G:/text/tmall/keyword.txt", ",");
        Spider spider = Spider.create(new TmallPageProcessor(new CrawlerInfo()))
                .addPipeline(new AnswerPopeline("G:/text/tmall/data/"));

        for (SkuData skuData : skuDatas) {
            Request request = new Request();

//            hanleSearchRequest(request,skuData);
//            hanleCommentRequest(request, skuData);

//            request.setUrl(listUrl);

//            request.setUrl("https://shiseido.tmall.com/i/asynSearch.htm?_ksTS=1573462369785_163&mid=w-15884659123-0&wid=15884659123&path=/search.htm&search=y&spm=a1z10.3-b-s.w4011-15884659123.94.777c4d47tzyqEg&scene=taobao_shop&orderType=null&tsearch=y");

//
//            String host = hanleUrl("");

            request.putExtra("brand", skuData.getBrand());
//            request.putExtra("gongxiao", skuData.getUrl());
//            request.putExtra("brand", skuData.getWords());
//            request.putExtra("number", 1);
//            request.setUrl(skuData.getWords().replace("&sort=s","&sort=d"));
            request.setUrl(skuData.getBrand());
//            request.putExtra("host", host);
            spider.addRequest(request);

//            spider.setDownloader(new SougouWeixinDownloader());


        }

        spider.thread(5);
        spider.run();
    }

    private static void hanleCommentRequest(Request request, SkuData skuData) {
        request.setUrl(skuData.getUrl());
        request.putExtra("brand", skuData.getBrand());
        request.putExtra("title", skuData.getWords());
        request.putExtra("xiaoliang", skuData.str4);
        request.putExtra("price", skuData.str5);
        request.putExtra("commentNumber", skuData.str6);

    }

    private static void hanleSearchRequest(Request request, SkuData skuData) {
        request.setUrl(listUrl + UrlUtils.encodeStr(skuData.getBrand()));
        request.putExtra("brand", skuData.getBrand());
    }


    private static String hanleUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            return "";
        }
        return RegexUtil.findFirstGroup("https://.*.tmall.com", url);
    }


}
