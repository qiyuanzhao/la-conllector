package com.lavector.collector.crawler.project.game.dota2.pcgames;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.project.game.dota2.pcgames.page.PCGamesNewsListPage;
import com.lavector.collector.crawler.project.game.dota2.pcgames.page.PCGamesNewsPage;
import us.codecraft.webmagic.Site;

/**
 * Created on 2017/10/20.
 *
 * @author zeng.zhao
 */
public class PCGamesPageProcessor extends BaseProcessor {

    private Site site = Site.me().setCharset("gbk");

    public PCGamesPageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new PCGamesNewsListPage(), new PCGamesNewsPage());
    }

    @Override
    public Site getSite() {
        return site;
    }
}
