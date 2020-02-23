package com.lavector.collector.crawler.project.weibo.weiboAllContent;

import com.lavector.collector.crawler.project.weibo.weiboAllContent.newPage.MyFilePipeline;
import com.lavector.collector.crawler.project.weibo.weiboAllContent.newPage.ReadCsv;
import com.lavector.collector.crawler.project.weibo.weiboAllContent.newPage.SkuData;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;

import java.util.List;


public class WeiboCrawler {

    public static void main(String[] args) {

        List<SkuData> skuDatas = ReadCsv.getSkuData();

        Spider spider = Spider.create(new WeibolPageProcessor())
                .addPipeline(new MyFilePipeline("G:/text/data"));

        for (SkuData skudata : skuDatas) {
            Request request = new Request();
            request.setUrl(skudata.getUrl());
            request.putExtra("skuData", skudata);
            spider.addRequest(request);
        }

        spider.start();
    }
}
