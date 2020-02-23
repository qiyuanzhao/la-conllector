package com.lavector.collector.crawler.project.yanying.bilibili;

import com.google.common.collect.Lists;
import com.lavector.collector.crawler.base.BaseCrawler;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.MySpider;
import com.lavector.collector.crawler.util.StringToDateConverter;
import org.apache.commons.lang3.time.DateUtils;
import us.codecraft.webmagic.Request;

import java.util.Date;
import java.util.List;

/**
 * Created on 2018/2/8.
 *
 * @author zeng.zhao
 */
public class BiliBiliCrawler extends BaseCrawler {

    private static final StringToDateConverter converter = new StringToDateConverter();


    public static final String OUT_FILE = "/Users/zeng.zhao/Desktop/bilibili.json";

    public static final Date startTime = converter.convert("2017-10-01");

    public static final Date endTime = converter.convert("2017-12-31");

    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {
        MySpider spider = MySpider.createUseProxy(new BiliBiliPageProcessor(crawlerInfo));
        spider.setScheduler(new MyScheduler());
        spider.thread(20);
        return spider;
    }

    //眼影
    private List<Request> getStartRequests() {
        return Lists.newArrayList(new Request("https://search.bilibili.com/api/search?search_type=all&keyword=%E7%9C%BC%E5%BD%B1&from_source=banner_search&page=1"));
    }

    private List<Request> testRequests() {
        // https://www.bilibili.com/video/av18935020/
        // https://api.bilibili.com/x/v2/reply?callback=&jsonp=jsonp&pn=1&type=1&oid=18935020&sort=0
        return Lists.newArrayList(new Request("https://www.bilibili.com/video/av18935020/"));
    }

    public static void main(String[] args) {
        BiliBiliCrawler crawler = new BiliBiliCrawler();
        MySpider spider = crawler.createSpider(new CrawlerInfo());
        spider.startRequest(crawler.getStartRequests());
        spider.start();
    }
}
