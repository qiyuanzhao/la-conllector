package com.lavector.collector.crawler.project.weibo.weiboSearchContent;


import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.project.weibo.weiboPerson.WeiboPersonProcesser;
import com.lavector.collector.crawler.project.weibo.weiboPerson.page.WeiboPersonDownloader;
import com.lavector.collector.crawler.project.weibo.weiboPerson.pipeline.PersonPipeline;
import com.lavector.collector.crawler.util.ReadTextUtils;
import com.lavector.collector.crawler.util.UrlUtils;
import com.lavector.collector.entity.readData.SkuData;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.Downloader;

import java.util.LinkedList;
import java.util.List;

public class WeiboCrawler {

    public static void main(String[] args) {

        List<SkuData> skuDatas = ReadTextUtils.getSkuData("G:/text/newWeibo/qqq.txt", ",");

        Spider spider = Spider.create(new WeiboContentProcesser(new CrawlerInfo()));

        int i = 1;
        for (SkuData skudata : skuDatas) {

            skudata.setHeadWords(getList());

            Request request = new Request();
            System.out.println("第" + i + "个");
//            String newUrl = handle(skudata.getBrand().trim());

            String newUrl = skudata.getBrand();
            System.out.println(newUrl);
            if (!"".equals(newUrl)) {
                request.setUrl(newUrl);
                request.putExtra("skuData", skudata);
                spider.addRequest(request);
            }
            i++;
        }

        spider.thread(9);
        Downloader downloader = new WeiboPersonDownloader();
//        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
//        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(getListProxy()));
//        spider.setDownloader(httpClientDownloader);
//        spider.setDownloader(downloader);
        spider.start();
    }

    public static List<String> getList() {
        List<String> list = new LinkedList<>();
        list.add("用户id");
        list.add("姓名");
        return list;
    }

    private static String handle(String keyword) {
        return "https://s.weibo.com/user?q=" + UrlUtils.encode(keyword);

    }

}
