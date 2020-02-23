package com.lavector.collector.crawler.project.game.overwatch.duowan;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.project.game.overwatch.duowan.page.NewsListPage;
import com.lavector.collector.crawler.project.game.overwatch.duowan.page.NewsPage;
import us.codecraft.webmagic.Site;

/**
 * Created on 2017/10/25.
 *
 * @author zeng.zhao
 */
public class DuoWanPageProcessor extends BaseProcessor {

    private Site site = Site.me().setCharset("utf-8");

    public DuoWanPageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new NewsListPage(), new NewsPage());
    }

    @Override
    public Site getSite() {
        return site;
    }
}
