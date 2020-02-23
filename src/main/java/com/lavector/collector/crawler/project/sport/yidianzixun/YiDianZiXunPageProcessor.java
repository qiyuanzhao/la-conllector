package com.lavector.collector.crawler.project.sport.yidianzixun;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.project.sport.yidianzixun.page.ZiXunNewsListPage;
import com.lavector.collector.crawler.project.sport.yidianzixun.page.ZiXunNewsPage;

/**
 * Created on 2017/10/17.
 *
 * @author zeng.zhao
 */
public class YiDianZiXunPageProcessor extends BaseProcessor {

    public YiDianZiXunPageProcessor (CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new ZiXunNewsListPage(), new ZiXunNewsPage());
    }
}
