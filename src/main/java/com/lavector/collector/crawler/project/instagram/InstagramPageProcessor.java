package com.lavector.collector.crawler.project.instagram;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import us.codecraft.webmagic.Site;

/**
 * Created on 2018/4/16.
 *
 * @author zeng.zhao
 */
public class InstagramPageProcessor extends BaseProcessor{

    private Site site = Site.me()
            .setCharset("utf-8")
            .setRetrySleepTime(4000)
            .setCycleRetryTimes(5)
            .setTimeOut(15 * 1000)
            .setSleepTime(4000);

    public InstagramPageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new InstagramHotPage());
    }

    @Override
    public Site getSite() {
        site.addHeader("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader());
        site.addHeader("user-agent", getUserAgent());
        return site;
    }
}
