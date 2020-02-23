package com.lavector.collector.crawler.project.tieba;


import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.project.gengmei.GengmeiProcesser;
import com.lavector.collector.crawler.project.gengmei.pipepine.UserDiaryPipeline;
import com.lavector.collector.crawler.project.weibo.weiboPerson.page.SougouWeixinDownloader;
import com.lavector.collector.crawler.util.ReadTextUtils;
import com.lavector.collector.entity.readData.SkuData;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;

import java.util.List;

public class TiebaCrawler {


    public static void main(String[] args) {

        List<SkuData> skuDatas = ReadTextUtils.getSkuData("G:/text/gengmei/test.txt", ",");


        Spider spider = Spider.create(new GengmeiProcesser(new CrawlerInfo()))
                .addPipeline(new UserDiaryPipeline("G:/text/gengmei/data/diary/detile"));
        for (SkuData skuData : skuDatas){
            String url = skuData.getUrl();
            Request request = new Request(url);
            request.putExtra("skuData",skuData);
            spider.addRequest(request);
        }
//        spider.setDownloader(new SougouWeixinDownloader());
        spider.thread(20);
        spider.start();
    }


}
