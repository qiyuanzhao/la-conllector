package com.lavector.collector.crawler.project.article.yidian;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.project.article.yidian.page.ArticlePage;
import com.lavector.collector.crawler.project.article.yidian.page.SearchListPage;

/**
 * Created on 2017/12/27.
 *
 * @author zeng.zhao
 */
public class YiDianPageProcessor extends BaseProcessor {

    public YiDianPageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new SearchListPage(), new ArticlePage());
    }
}
