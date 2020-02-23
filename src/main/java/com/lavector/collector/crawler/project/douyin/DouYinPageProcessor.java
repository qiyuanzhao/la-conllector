package com.lavector.collector.crawler.project.douyin;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import us.codecraft.webmagic.Site;

/**
 * Created on 2018/4/4.
 *
 * @author zeng.zhao
 */
public class DouYinPageProcessor extends BaseProcessor {

    private Site site = Site.me()
            .setCharset("utf-8")
            .setRetrySleepTime(4000)
            .setCycleRetryTimes(5)
            .setTimeOut(15 * 1000)
            .setSleepTime(4000);

    DouYinPageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new DouYinSearchUserListPage(), new DouYinUserHomePage());
    }

    @Override
    public Site getSite() {
        site.addHeader("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader());
        site.addHeader("user-agent", getUserAgent());
        return site;
    }
}
