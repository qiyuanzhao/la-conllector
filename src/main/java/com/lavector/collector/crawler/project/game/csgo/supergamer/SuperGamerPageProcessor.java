package com.lavector.collector.crawler.project.game.csgo.supergamer;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.project.game.csgo.supergamer.page.NewsListPage;
import com.lavector.collector.crawler.project.game.csgo.supergamer.page.NewsPage;
import us.codecraft.webmagic.Site;

/**
 * Created on 2017/10/23.
 *
 * @author zeng.zhao
 */
public class SuperGamerPageProcessor extends BaseProcessor {

    private Site site = Site.me().setCharset("utf-8");

    public SuperGamerPageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new NewsListPage(), new NewsPage());
    }

    @Override
    public Site getSite() {
        return site;
    }
}
