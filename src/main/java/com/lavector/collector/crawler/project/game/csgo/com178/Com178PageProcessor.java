package com.lavector.collector.crawler.project.game.csgo.com178;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.project.game.csgo.com178.page.NewsListPage;
import com.lavector.collector.crawler.project.game.csgo.com178.page.NewsPage;

/**
 * Created on 2017/10/20.
 *
 * @author zeng.zhao
 */
public class Com178PageProcessor extends BaseProcessor {

    public Com178PageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new NewsListPage(), new NewsPage());
    }
}
