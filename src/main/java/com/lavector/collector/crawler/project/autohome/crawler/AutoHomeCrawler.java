package com.lavector.collector.crawler.project.autohome.crawler;


import com.lavector.collector.crawler.base.BaseCrawler;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.MySpider;
import com.lavector.collector.crawler.project.autohome.page.AutoHomePipeline;
import com.lavector.collector.crawler.project.autohome.page.DetilePage;
import com.lavector.collector.crawler.project.autohome.page.LuntanSearchPage;
import com.lavector.collector.crawler.project.autohome.page.TitlePage;
import com.lavector.collector.crawler.util.ReadTextUtils;
import com.lavector.collector.entity.readData.SkuData;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;

import java.util.LinkedList;
import java.util.List;


public class AutoHomeCrawler extends BaseCrawler {


    public static void main(String[] args) {

        List<SkuData> skuDataList = ReadTextUtils.getSkuData("G:/text/autohome/url.txt", ",");

        Spider spider = Spider.create(new AutoHomeProcessor(new CrawlerInfo(),new LuntanSearchPage(),new DetilePage(),new TitlePage()));

        for (SkuData skuData : skuDataList) {
//            skuData.setHeadWords(skuData.getWords());
            skuData.setHeadWords(getList());
            Request request = new Request();
//            String newUrl = handleUrl(skuData.getUrl(),request);
            request.setUrl(skuData.getUrl());
            request.putExtra("skuData", skuData);

            spider.addRequest(request);
        }
//        spider.setScheduler(new RedisScheduler("localhost"));
        spider.addPipeline(new AutoHomePipeline("G:/text/autohome/data"));
        spider.thread(3);
        spider.start();
    }

    public static List<String> getList() {
        List<String> list = new LinkedList<>();

        list.add("关键词");
        list.add("url");
        list.add("发帖人");
        list.add("日期");
        list.add("标题");
        list.add("回复数");

        list.add("点击");
        list.add("详情url");
        list.add("内容");
        return list;
    }


    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {
        return null;
    }
}
