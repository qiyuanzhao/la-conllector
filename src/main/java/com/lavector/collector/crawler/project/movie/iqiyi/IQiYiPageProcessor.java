package com.lavector.collector.crawler.project.movie.iqiyi;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import com.lavector.collector.crawler.project.movie.iqiyi.page.*;
import us.codecraft.webmagic.Site;

/**
 * Created on 2018/1/24.
 *
 * @author zeng.zhao
 */
public class IQiYiPageProcessor extends BaseProcessor {

    private Site site = Site.me()
            .setCharset("utf-8")
            .setRetrySleepTime(4000)
            .setCycleRetryTimes(5)
            .setTimeOut(15 * 1000)
            .setSleepTime(4000);

    IQiYiPageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new CategoryListPage(), new VideoPage(), new SingleCategoryPage(),
            new VideoInfoJsonPage());
    }

    @Override
    public Site getSite() {
        site.addHeader("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader());
        site.addHeader("user-agent", getUserAgent());
        return site;
    }
}
