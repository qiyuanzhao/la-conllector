package com.lavector.collector.crawler.project.edu.mama;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.project.edu.mama.page.BBSListPage;
import com.lavector.collector.crawler.project.edu.mama.page.MessagePage;
import us.codecraft.webmagic.Site;

/**
 * Created on 2017/11/16.
 *
 * @author zeng.zhao
 */
public class MaMaPageProcessor extends BaseProcessor {

    private Site site = Site.me()
            .setSleepTime(1500)
            .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");

    public MaMaPageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new BBSListPage(), new MessagePage());
    }

    @Override
    public Site getSite() {
        return site;
    }
}
