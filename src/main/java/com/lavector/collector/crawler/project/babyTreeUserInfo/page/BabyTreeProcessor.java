package com.lavector.collector.crawler.project.babyTreeUserInfo.page;


import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.project.babyTreeUserInfo.page.DetilePage;
import com.lavector.collector.crawler.project.babyTreeUserInfo.page.UserInfoPage;
import us.codecraft.webmagic.Site;

public class BabyTreeProcessor extends BaseProcessor {

    private Site site = Site.me()
            .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3573.0 Safari/537.36")
//            .addHeader("cookie","")
            .setRetrySleepTime(5000)
            .setSleepTime(1000)
            .setCycleRetryTimes(5);

    public BabyTreeProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo,new DetilePage(),new UserInfoPage());
    }


    @Override
    public Site getSite() {
        return site;
    }
}
