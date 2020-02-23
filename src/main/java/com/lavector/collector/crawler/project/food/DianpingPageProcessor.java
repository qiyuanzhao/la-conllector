package com.lavector.collector.crawler.project.food;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.food.page.*;
import us.codecraft.webmagic.Site;

/**
 * Created on 2017/11/6.
 *
 * @author zeng.zhao
 */
public class DianpingPageProcessor extends BaseProcessor {

    private Site site = Site.me()
            .setCharset("utf-8")
            .setRetrySleepTime(4000)
            .setCycleRetryTimes(5)
            .setTimeOut(15 * 1000)
            .setSleepTime(4000);

    public DianpingPageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new ShopListByRegionPage(), new DishListPage(),
                new ShopListPageBySearch()
                );
    }

    @Override
    public Site getSite() {
        site.addHeader("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader());
        site.addHeader("user-agent", getUserAgent());
        return site;
    }
}
