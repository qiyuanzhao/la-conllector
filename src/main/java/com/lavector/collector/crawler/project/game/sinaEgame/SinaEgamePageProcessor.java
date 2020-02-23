package com.lavector.collector.crawler.project.game.sinaEgame;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.project.game.sinaEgame.page.EgameNewsTop;
import com.lavector.collector.crawler.project.game.sinaEgame.page.SinaEgameNewsContent;

/**
 * Created on 26/09/2017.
 *
 * @author seveniu
 */
public class SinaEgamePageProcessor extends BaseProcessor {
    public SinaEgamePageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new EgameNewsTop(), new SinaEgameNewsContent());
    }

//    @Override
//    public Site getSite() {
//        Site site = Site.me()
//                .setCycleRetryTimes(3)
//                .setTimeOut(10 * 1000)
//                .setSleepTime(1000)
//                .setCharset("GB2312");
//        return site;
//    }
}
