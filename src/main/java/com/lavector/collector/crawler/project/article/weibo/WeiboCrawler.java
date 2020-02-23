package com.lavector.collector.crawler.project.article.weibo;

import com.lavector.collector.crawler.base.BaseCrawler;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.MySpider;

/**
 * Created on 2017/12/22.
 *
 * @author zeng.zhao
 */
public class WeiboCrawler extends BaseCrawler {

    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {
        MySpider spider = MySpider.create(new WeiboPageProcessor(crawlerInfo));
        spider.thread(5);
        return spider;
    }
}
