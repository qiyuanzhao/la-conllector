package com.lavector.collector.crawler.project.iqiyi;


import com.lavector.collector.crawler.base.BaseCrawler;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.MySpider;
import com.lavector.collector.crawler.project.weixinnew.WeixinNewDownloader;
import com.lavector.collector.crawler.project.weixinnew.WeixinNewProcesser;
import com.lavector.collector.crawler.util.ReadTextUtils;
import com.lavector.collector.crawler.util.UrlUtils;
import com.lavector.collector.entity.readData.SkuData;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.Downloader;

import java.util.List;

public class IQiYiCrawler extends BaseCrawler {


    public static void main(String[] args) {
        List<SkuData> skuDatas = ReadTextUtils.getSkuData("G:/text/iqiyi/keyword.txt", ",");
        Spider spider = Spider.create(new IQiYiProcesser(new CrawlerInfo()));

        for (SkuData skuData : skuDatas) {
            String brand = skuData.getBrand();
//            String url = skuData.getUrl();
            Request request = new Request("https://so.iqiyi.com/so/q_" + UrlUtils.encode(brand) + "_ctg__t_0_page_1_p_1_qc_0_rd__site__m_4_bitrate_").putExtra("brand", brand).putExtra("pageNumber", 1);
            spider.addRequest(request);

            spider.thread(2);
            Downloader downloader = new IQiYiDownloader();
            spider.setDownloader(downloader);
            spider.run();
        }





    }

    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {
        return null;
    }
}
