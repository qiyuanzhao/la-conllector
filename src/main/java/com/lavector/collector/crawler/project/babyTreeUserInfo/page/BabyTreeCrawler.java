package com.lavector.collector.crawler.project.babyTreeUserInfo.page;


import com.lavector.collector.crawler.base.BaseCrawler;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.MySpider;
import com.lavector.collector.crawler.util.ReadTextUtils;
import com.lavector.collector.entity.readData.SkuData;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;

import java.util.LinkedList;
import java.util.List;

@RabbitListener(queues = "test")
public class BabyTreeCrawler extends BaseCrawler {


    @RabbitHandler
    public void showMeagges(String url){
        System.out.println(url);
    }

    public void startCrawler() {

        List<SkuData> skuDataList = ReadTextUtils.getSkuData("G:/text/babyTree/text.txt", ",");

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
        list.add("url");
        list.add("年龄");
        list.add("地区");
        list.add("收藏数");
        list.add("内容");

        list.add("评论用户");
        list.add("宝宝年龄");
        list.add("地区");
        list.add("评论时间");
        list.add("内容");
        return list;
    }


    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {

        return null;
    }

}
