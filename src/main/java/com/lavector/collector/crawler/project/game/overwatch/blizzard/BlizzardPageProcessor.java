package com.lavector.collector.crawler.project.game.overwatch.blizzard;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.game.overwatch.blizzard.page.NewsListPage;
import com.lavector.collector.crawler.project.game.overwatch.blizzard.page.NewsPage;

/**
 * Created on 2017/10/24.
 *
 * @author zeng.zhao
 */
public class BlizzardPageProcessor extends BaseProcessor {

    public BlizzardPageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new NewsListPage(), new NewsPage());
    }
}
