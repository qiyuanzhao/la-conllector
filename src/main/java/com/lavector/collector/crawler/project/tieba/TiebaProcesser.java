package com.lavector.collector.crawler.project.tieba;


import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import com.lavector.collector.crawler.base.PageParse;
import us.codecraft.webmagic.Site;

import java.util.Random;

public class TiebaProcesser extends BaseProcessor {


    public TiebaProcesser(CrawlerInfo crawlerInfo, PageParse... pageParses) {
        super(crawlerInfo, pageParses);
    }


    private static Site site = Site.me()
            .setCycleRetryTimes(3)
            .setCharset("utf-8")
//            .addHeader("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader())
            .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:65.0) Gecko/20100101 Firefox/65.0")
            .setTimeOut(10 * 1000);



    @Override
    public Site getSite() {
        Random random = new Random();
        int time = random.nextInt(10);
        return site.setSleepTime(1000);
    }


}
