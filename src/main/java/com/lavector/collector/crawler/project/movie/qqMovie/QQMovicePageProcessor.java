package com.lavector.collector.crawler.project.movie.qqMovie;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.project.movie.qqMovie.page.MovieNews;
import com.lavector.collector.crawler.project.movie.qqMovie.page.MovieNewsContent;
import us.codecraft.webmagic.Site;

/**
 * Created on 26/09/2017.
 *
 * @author seveniu
 */
public class QQMovicePageProcessor extends BaseProcessor {
    public QQMovicePageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new MovieNews(), new MovieNewsContent());
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
