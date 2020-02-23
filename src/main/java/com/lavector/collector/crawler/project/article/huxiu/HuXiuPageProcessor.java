package com.lavector.collector.crawler.project.article.huxiu;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.project.article.huxiu.page.ArticlePage;
import com.lavector.collector.crawler.project.article.huxiu.page.SearchListPage;
import us.codecraft.webmagic.Site;

/**
 * Created on 2017/12/26.
 *
 * @author zeng.zhao
 */
public class HuXiuPageProcessor extends BaseProcessor {

    private Site site = Site.me()
            .setCycleRetryTimes(3)
            .setRetrySleepTime(1500)
            .setSleepTime(1500)
            .setTimeOut(15 * 1000);

    public HuXiuPageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new SearchListPage(), new ArticlePage());
    }

    @Override
    public Site getSite() {
        return site;
    }
}
