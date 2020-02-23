package com.lavector.collector.crawler.project.dazhongdianping;

import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.project.tmall.TmallPageProcessor;
import com.lavector.collector.crawler.project.weibo.weiboPerson.page.SougouWeixinDownloader;
import com.lavector.collector.crawler.project.zhihu.pipepine.AnswerPopeline;
import com.lavector.collector.crawler.util.ReadTextUtils;
import com.lavector.collector.crawler.util.UrlUtils;
import com.lavector.collector.entity.readData.SkuData;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;

import java.util.List;

/**
 * Created by qyz on 2019/8/7.
 */
public class DianpingCrawler {
    private static String listUrl = "https://www.dianping.com/search/keyword/2/0_";

    public static void main(String[] args) {
        List<SkuData> skuDatas = ReadTextUtils.getSkuData("G:/text/dianping/txt.txt", ",");
        Spider spider = Spider.create(new DianpingPageProcessor(new CrawlerInfo()));

        for (SkuData skuData : skuDatas) {
            Request request = new Request();
//            request.setUrl(listUrl + UrlUtils.encodeStr(skuData.getBrand()));
            request.setUrl(skuData.getBrand());
//            request.putExtra("brand", skuData.getBrand());

            spider.addRequest(request.putExtra("keyword", skuData.getBrand()));
//            spider.setDownloader(new SougouWeixinDownloader());
            spider.thread(1);
            spider.run();
        }
    }


}
