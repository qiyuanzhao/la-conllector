package com.lavector.collector.crawler.project.sport.tencent;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.project.sport.tencent.page.FootballNewsListPage;
import com.lavector.collector.crawler.project.sport.tencent.page.FootballNewsPage;

/**
 * Created on 2017/10/18.
 *
 * @author zeng.zhao
 */
public class TencentFootballPageProcessor extends BaseProcessor {

    public TencentFootballPageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new FootballNewsListPage(), new FootballNewsPage());
    }
}
