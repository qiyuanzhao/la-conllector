package com.lavector.collector.crawler.project.weixinmall;


import com.lavector.collector.crawler.base.BaseCrawler;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.MySpider;
import com.lavector.collector.crawler.util.ReadTextUtils;
import com.lavector.collector.crawler.util.UrlUtils;
import com.lavector.collector.entity.readData.SkuData;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;

import java.util.List;

public class WeixinMallCrawler extends BaseCrawler {


    public static void main(String[] args) {
        List<SkuData> skuDatas = ReadTextUtils.getSkuData("G:/text/newWeixin/weixinmall/keyword.txt", ",");
        Spider spider = Spider.create(new WeixinMallProcesser(new CrawlerInfo()));

        for (SkuData skuData : skuDatas) {
            String brand = skuData.getBrand();
            String url1 = skuData.getUrl();
            String url = "http://open.koldata.net/wechat/simple/search?keyword=" + UrlUtils.encode(brand) + "&page=1";
//            String url = "http://open.koldata.net/wechat/articles?wechat_id=" + url1 + "&page=1";
            Request request = new Request(url).putExtra("brand", brand);
            spider.addRequest(request);
        }


        spider.thread(2);
//        Downloader downloader = new SougouWeixinDownloader();
//        spider.setDownloader(downloader);
        spider.start();


    }

    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {
        return null;
    }
}
