package com.lavector.collector.crawler.project.sport.qqSport;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.project.sport.qqSport.page.SportNewsContent;
import com.lavector.collector.crawler.project.sport.qqSport.page.SportTop;
import us.codecraft.webmagic.Site;

/**
 * Created on 26/09/2017.
 *
 * @author seveniu
 */
public class TencentSportPageProcessor extends BaseProcessor {
    public TencentSportPageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new SportTop(), new SportNewsContent());
    }

    @Override
    public Site getSite() {
        Site site = Site.me()
                .setCycleRetryTimes(3)
                .setTimeOut(10 * 1000)
                .setSleepTime(1000)
                .setCharset("GB2312");
        return site;
    }
}
