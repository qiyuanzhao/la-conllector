package com.lavector.collector.crawler.project.sport.nba.nbaChina;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.project.sport.nba.nbaChina.page.NBAChinaNewsListPage;
import com.lavector.collector.crawler.project.sport.nba.nbaChina.page.NBAChinaNewsPage;

/**
 * Created on 2017/10/17.
 *
 * @author zeng.zhao
 */
public class NBAChinaPageProcessor extends BaseProcessor {

    public NBAChinaPageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new NBAChinaNewsListPage(), new NBAChinaNewsPage());
    }
}
