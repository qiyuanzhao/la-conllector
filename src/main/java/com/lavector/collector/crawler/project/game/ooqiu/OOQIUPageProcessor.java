package com.lavector.collector.crawler.project.game.ooqiu;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.project.game.ooqiu.page.NewsListPage;
import com.lavector.collector.crawler.project.game.ooqiu.page.NewsPage;
import us.codecraft.webmagic.Site;

/**
 * Created on 2017/10/23.
 *
 * @author zeng.zhao
 */
public class OOQIUPageProcessor extends BaseProcessor {

    private Site site = Site.me()
            .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36")
            .setCharset("gbk");

    public OOQIUPageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new NewsListPage(), new NewsPage());
    }

    @Override
    public Site getSite() {
        return site;
    }
}
