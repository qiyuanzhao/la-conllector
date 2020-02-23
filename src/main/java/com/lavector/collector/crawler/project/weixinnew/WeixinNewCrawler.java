package com.lavector.collector.crawler.project.weixinnew;


import com.lavector.collector.crawler.base.BaseCrawler;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.MySpider;
import com.lavector.collector.crawler.project.weixinmall.WeixinMallProcesser;
import com.lavector.collector.crawler.util.ReadTextUtils;
import com.lavector.collector.crawler.util.UrlUtils;
import com.lavector.collector.entity.readData.SkuData;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.Downloader;

import java.util.List;

public class WeixinNewCrawler extends BaseCrawler {


    public static void main(String[] args) {
        List<SkuData> skuDatas = ReadTextUtils.getSkuData("G:/text/weixinnew/keyword.txt", ",");
        Spider spider = Spider.create(new WeixinNewProcesser(new CrawlerInfo()));

        for (SkuData skuData : skuDatas) {
            String brand = skuData.getBrand();
//            String url = skuData.getUrl();
            Request request = new Request("https://news.sogou.com/news?&clusterId=&p=42230305&time=0&query=" + UrlUtils.encode(brand) + "&mode=1&media=&sort=1&page=1").putExtra("brand", brand);
            spider.addRequest(request);
        }


        spider.thread(5);
        Downloader downloader = new WeixinNewDownloader();
        spider.setDownloader(downloader);
        spider.start();


    }

    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {
        return null;
    }
}
