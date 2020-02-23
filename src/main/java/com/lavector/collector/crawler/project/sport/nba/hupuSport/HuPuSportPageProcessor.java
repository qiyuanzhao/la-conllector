package com.lavector.collector.crawler.project.sport.nba.hupuSport;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.project.sport.nba.hupuSport.page.HuPuSportNewsListPage;
import com.lavector.collector.crawler.project.sport.nba.hupuSport.page.HuPuSportNewsPage;

/**
 * Created on 2017/10/16.
 *
 * @author zeng.zhao
 */
public class HuPuSportPageProcessor extends BaseProcessor {

    public HuPuSportPageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new HuPuSportNewsListPage(), new HuPuSportNewsPage());
    }
}
