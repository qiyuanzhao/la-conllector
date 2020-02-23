package com.lavector.collector.crawler.project.article.toutiao;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.article.toutiao.page.ArticleListPage;
import com.lavector.collector.crawler.project.article.toutiao.page.ArticlePage;
import com.lavector.collector.crawler.project.article.toutiao.page.DefaultArticlePage;
import com.lavector.collector.crawler.project.article.toutiao.page.ThirdSourcePage;
import us.codecraft.webmagic.Site;

/**
 * Created on 2017/12/22.
 *
 * @author zeng.zhao
 */
public class TouTiaoPageProcessor extends BaseProcessor {

    private Site site = Site.me()
            .setSleepTime(1500)
            .setTimeOut(15 * 1000)
            .setCycleRetryTimes(3)
            .setRetrySleepTime(1500);
    public TouTiaoPageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new ArticleListPage(), new ArticlePage(), new ThirdSourcePage(), new DefaultArticlePage());
    }

    @Override
    public Site getSite() {
        return site;
    }
}
