package com.lavector.collector.crawler.project.game.overwatch.pcgames;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.game.overwatch.pcgames.page.NewsListPage;
import com.lavector.collector.crawler.project.game.overwatch.pcgames.page.NewsPage;
import us.codecraft.webmagic.Site;

/**
 * Created on 2017/10/23.
 *
 * @author zeng.zhao
 */
public class PCGamesPageProcessor extends BaseProcessor {

    private Site site = Site.me().setCharset("gbk");

    public PCGamesPageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new NewsListPage(), new NewsPage());
    }

    @Override
    public Site getSite() {
        return site;
    }
}
