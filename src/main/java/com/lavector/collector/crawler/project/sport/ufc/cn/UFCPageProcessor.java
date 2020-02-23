package com.lavector.collector.crawler.project.sport.ufc.cn;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.project.sport.ufc.cn.page.UFCNewsListPage;
import com.lavector.collector.crawler.project.sport.ufc.cn.page.UFCNewsPage;
import us.codecraft.webmagic.Site;

/**
 * Created on 2017/10/19.
 *
 * @author zeng.zhao
 */
public class UFCPageProcessor extends BaseProcessor {

    private Site site = Site.me().setCharset("utf-8");

    public UFCPageProcessor (CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new UFCNewsListPage(), new UFCNewsPage());
    }

    @Override
    public Site getSite() {
        return site;
    }
}
