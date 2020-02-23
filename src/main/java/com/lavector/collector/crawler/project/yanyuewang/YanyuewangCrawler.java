package com.lavector.collector.crawler.project.yanyuewang;

import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.project.weibo.weiboPerson.page.SougouWeixinDownloader;
import com.lavector.collector.crawler.project.zhihu.ZhihuProcesser;
import com.lavector.collector.crawler.project.zhihu.pipepine.AnswerPopeline;
import com.lavector.collector.crawler.util.ReadTextUtils;
import com.lavector.collector.entity.readData.SkuData;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.Scheduler;

import java.util.List;

/**
 * Created by qyz on 2019/8/26.
 */
public class YanyuewangCrawler {


    public static void main(String[] args) {
        List<SkuData> skuDatas = ReadTextUtils.getSkuData("G:/text/yanyuewang/question.txt", ",");

        Spider spider = Spider.create(new ZhihuProcesser(new CrawlerInfo()));

        spider.setScheduler(new QueueScheduler());

        String uuid = spider.getUUID();

        for (SkuData skuData : skuDatas) {
            spider.addRequest(new Request());
        }
        spider.setDownloader(new SougouWeixinDownloader()).
                thread(10).
                start();

    }




}
