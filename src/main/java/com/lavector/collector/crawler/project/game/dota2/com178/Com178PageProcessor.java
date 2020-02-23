package com.lavector.collector.crawler.project.game.dota2.com178;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.project.game.dota2.com178.page.NewsListPage;
import com.lavector.collector.crawler.project.game.dota2.com178.page.NewsPage;

/**
 * Created on 2017/10/23.
 *
 * @author zeng.zhao
 */
class Com178PageProcessor extends BaseProcessor {

    Com178PageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new NewsListPage(), new NewsPage());
    }
}
