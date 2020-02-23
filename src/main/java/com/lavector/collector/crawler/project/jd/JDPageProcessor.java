package com.lavector.collector.crawler.project.jd;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.project.jd.page.JDCommentPage;
import com.lavector.collector.crawler.project.jd.page.JDItemPage;
import com.lavector.collector.crawler.project.jd.page.JDSearchPage;
import us.codecraft.webmagic.Site;

/**
 * Created on 2018/1/17.
 *
 * @author zeng.zhao
 */
public class JDPageProcessor extends BaseProcessor {

    private Site site = Site.me()
            .setRetrySleepTime(2000)
            .setSleepTime(2000)
            .setCycleRetryTimes(3);

    public JDPageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new JDCommentPage(), new JDItemPage(), new JDSearchPage());
    }

    @Override
    public Site getSite() {
        return site;
    }
}
