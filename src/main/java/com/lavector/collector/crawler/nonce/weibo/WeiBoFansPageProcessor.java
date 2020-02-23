package com.lavector.collector.crawler.nonce.weibo;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import us.codecraft.webmagic.Site;

/**
 * Created on 11/05/2018.
 *
 * @author zeng.zhao
 */
public class WeiBoFansPageProcessor extends BaseProcessor {

    private Site site = Site.me()
            .setCycleRetryTimes(3)
            .setRetrySleepTime(2000)
            .setSleepTime(2000);

    WeiBoFansPageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo,
//                new FansListPage(),
                new FansMessageListPage(),
                new AjaxFansMessagePage(),
                new WeiBoCommentPage(),
                new FollowerListPage()
        );
    }

    @Override
    public Site getSite() {
        return site;
    }
}
