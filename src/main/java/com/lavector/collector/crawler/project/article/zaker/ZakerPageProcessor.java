package com.lavector.collector.crawler.project.article.zaker;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.project.article.zaker.page.ZakerArticlePage;
import com.lavector.collector.crawler.project.article.zaker.page.ZakerSearchListPage;

/**
 * Created on 2018/1/2.
 *
 * @author zeng.zhao
 */
public class ZakerPageProcessor extends BaseProcessor {


    public ZakerPageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new ZakerSearchListPage(), new ZakerArticlePage());
    }
}
