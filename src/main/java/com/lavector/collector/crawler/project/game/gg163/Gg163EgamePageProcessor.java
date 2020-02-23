package com.lavector.collector.crawler.project.game.gg163;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.project.game.gg163.page.NewsJsonPage;
import com.lavector.collector.crawler.project.game.gg163.page.NewsPlayPage;

/**
 * Created on 26/09/2017.
 *
 * @author seveniu
 */
public class Gg163EgamePageProcessor extends BaseProcessor {
    public Gg163EgamePageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new NewsJsonPage(), new NewsPlayPage());
    }
//
//    @Override
//    public Site getSite() {
//        Site site = Site.me()
//                .setCycleRetryTimes(3)
//                .setTimeOut(10 * 1000)
//                .setSleepTime(1000)
//                .setCharset(Charset.forName("GBK").displayName())
//                ;
//        return site;
//    }
}
