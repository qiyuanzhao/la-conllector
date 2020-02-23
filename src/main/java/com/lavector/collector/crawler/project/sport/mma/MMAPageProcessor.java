package com.lavector.collector.crawler.project.sport.mma;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.project.sport.mma.page.MMANewsHomePage;
import com.lavector.collector.crawler.project.sport.mma.page.MMANewsPage;
import us.codecraft.webmagic.Site;

/**
 * Created on 2017/10/18.
 *
 * @author zeng.zhao
 */
public class MMAPageProcessor extends BaseProcessor {

    private Site site = Site.me().setCharset("gbk");

    public MMAPageProcessor (CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new MMANewsHomePage(), new MMANewsPage());
    }

    @Override
    public Site getSite() {
        return site;
    }
}
