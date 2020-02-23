package com.lavector.collector.crawler.project.article.tianya;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.project.article.tianya.page.ArticlePage;
import com.lavector.collector.crawler.project.article.tianya.page.SearchListPage;

/**
 * Created on 2017/12/26.
 *
 * @author zeng.zhao
 */
public class TianYaPageProcessor extends BaseProcessor {

    public TianYaPageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new SearchListPage(), new ArticlePage());
    }
}
