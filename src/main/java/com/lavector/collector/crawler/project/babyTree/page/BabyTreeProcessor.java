package com.lavector.collector.crawler.project.babyTree.page;


import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Site;

@Component
public class BabyTreeProcessor extends BaseProcessor {

    private Site site = Site.me()
            .setRetrySleepTime(5000)
            .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3573.0 Safari/537.36")
//            .addHeader("cookie","")
            .setRetrySleepTime(2000)
            .setSleepTime(1000)
            .setCycleRetryTimes(3);

    public BabyTreeProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo,new SearchPage(),new DetilePage());
    }


    @Override
    public Site getSite() {
        return site;
    }
}
