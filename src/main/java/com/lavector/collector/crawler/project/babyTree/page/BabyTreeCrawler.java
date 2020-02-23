package com.lavector.collector.crawler.project.babyTree.page;



import com.lavector.collector.crawler.base.BaseCrawler;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.MySpider;
import com.lavector.collector.crawler.util.ReadTextUtils;
import com.lavector.collector.entity.readData.SkuData;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;

import java.util.LinkedList;
import java.util.List;

@Component
public class BabyTreeCrawler extends BaseCrawler {

    public void startCrawler() {

        List<SkuData> skuDataList = ReadTextUtils.getSkuData("G:/text/babyTree/template.txt", ",");

        Spider spider = Spider.create(new BabyTreeProcessor(new CrawlerInfo()));

        for (SkuData skuData : skuDataList) {
            skuData.setHeadWords(getList());
            Request request = new Request();
            request.setUrl(skuData.getUrl());
            request.putExtra("skuData", skuData);
            spider.addRequest(request);
        }
//        spider.setScheduler(new RedisScheduler("localhost"));
        spider.addPipeline(new MyFilePipeline("G:/text/babyTree/data"));
        spider.thread(20);
        spider.start();
    }

    public static List<String> getList() {
        List<String> list = new LinkedList<>();
        list.add("关键词");
        list.add("发帖用户");
        list.add("时间");
        list.add("浏览数");
        list.add("回复数");
        list.add("来自哪个圈子");
        list.add("Url");
        list.add("内容");
        return list;
    }


    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {

        return null;
    }



}
