package com.lavector.collector.crawler.nonce.douban;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import com.lavector.collector.crawler.base.PageParse;
import us.codecraft.webmagic.Site;

/**
 * Created on 05/06/2018.
 *
 * @author zeng.zhao
 */
public class DouBanPageProcessor extends BaseProcessor {

    private Site site = Site.me()
            .setCycleRetryTimes(3)
            .setRetrySleepTime(2000)
            .setSleepTime(2000)
            .setCharset("utf-8")
            .addHeader("user-agent", getUserAgent())
            .addHeader("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader());

    public DouBanPageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new DouBanMessageListPage());
    }

    @Override
    public Site getSite() {
        return site;
    }
}
