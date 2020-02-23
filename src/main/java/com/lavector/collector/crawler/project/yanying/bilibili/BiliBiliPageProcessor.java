package com.lavector.collector.crawler.project.yanying.bilibili;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.yanying.bilibili.page.*;
import us.codecraft.webmagic.Site;

/**
 * Created on 2018/2/8.
 *
 * @author zeng.zhao
 */
public class BiliBiliPageProcessor extends BaseProcessor {

    private Site site = Site.me()
            .setRetrySleepTime(4000)
            .setCycleRetryTimes(5)
            .setTimeOut(15 * 1000)
            .setSleepTime(4000)
            .setCharset("utf-8");

    public BiliBiliPageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo,
                new BiliSearchPage(),
                new BiliVideoPage(),
                new BiliCommentPage(),
                new BiliDanMuPage(),
                new BiliReplyPage()
            );
    }

    @Override
    public Site getSite() {
        site.addHeader("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader());
        site.addHeader("user-agent", getUserAgent());
        return site;
    }
}
