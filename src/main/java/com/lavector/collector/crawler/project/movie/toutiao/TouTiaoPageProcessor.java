package com.lavector.collector.crawler.project.movie.toutiao;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.movie.toutiao.page.NewsListPage;
import com.lavector.collector.crawler.project.movie.toutiao.page.NewsPage;

/**
 * Created on 2017/10/25.
 *
 * @author zeng.zhao
 */
public class TouTiaoPageProcessor extends BaseProcessor {

    public TouTiaoPageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new NewsListPage(), new NewsPage());
    }
}
