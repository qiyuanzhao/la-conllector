package com.lavector.collector.crawler.project.sport.sinaSport;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.project.sport.sinaSport.page.SinaSportNewsListPage;
import com.lavector.collector.crawler.project.sport.sinaSport.page.SinaSportNewsPage;

/**
 * Created on 26/09/2017.
 *
 * @author seveniu
 */
public class SinaSportPageProcessor extends BaseProcessor {
    public SinaSportPageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new SinaSportNewsListPage(), new SinaSportNewsPage());
    }
}
