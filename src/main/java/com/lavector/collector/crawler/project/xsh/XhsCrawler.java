package com.lavector.collector.crawler.project.xsh;


import com.lavector.collector.crawler.base.BaseCrawler;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.MySpider;
import com.lavector.collector.crawler.project.weibo.weiboPerson.page.SougouWeixinDownloader;
import com.lavector.collector.crawler.project.xsh.page.XhsPipeline;
import com.lavector.collector.crawler.util.ReadTextUtils;
import com.lavector.collector.entity.readData.SkuData;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.downloader.HttpClientDownloader;

import java.util.LinkedList;
import java.util.List;

public class XhsCrawler extends BaseCrawler {


    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {
        return null;
    }

    public static void main(String[] args) {

        List<SkuData> skuDatas = ReadTextUtils.getSkuData("G:/text/xhsSearch/url补充.txt", ",");

        Spider spider = Spider.create(new XhsProcesser(new CrawlerInfo())).addPipeline(new XhsPipeline("G:/text/xhsSearch"));

        for (SkuData skuData : skuDatas) {
//            skuData.setHeadWords(getDtileList());
            String url = "https://www.xiaohongshu.com/user/profile/" + skuData.getBrand();
            Request request = new Request(url);

//            Request request = new Request(skuData.getUrl());

//            request.putExtra("skuData", skuData);
            request.putExtra("name", skuData.getBrand());
            spider.addRequest(request);
        }
//        spider.addRequest(new Request("http://www.baidu.com/link?url=V8thPjUy1oUXNu6pcu4B0cD7AmUpzTmq_HOCSIKK7vjZ-LnCENwA-6AYl_zW1p5CrJrucrlt2kUBIrqwudu4kK6MxD1S39mLwfLKkoMyetejfuZrnnElWYOlv2wZyc_a"));


//        Downloader downloader = new SougouWeixinDownloader();
////
//        spider.setDownloader(downloader);
        spider.thread(4);
        spider.start();

    }

    public static List<String> getList() {
        List<String> list = new LinkedList<>();

        list.add("关键词");
        list.add("url");
        list.add("性别");
        list.add("年龄");
//        list.add("姓名");
        list.add("城市");
        return list;
    }

    public static List<String> getDtileList() {
        List<String> list = new LinkedList<>();

        list.add("关键词");
        list.add("url");
        list.add("性别");
        list.add("年龄");
        list.add("姓名");
        list.add("城市");
        return list;
    }


}
