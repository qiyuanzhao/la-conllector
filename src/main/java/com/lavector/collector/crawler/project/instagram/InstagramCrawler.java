package com.lavector.collector.crawler.project.instagram;

import com.lavector.collector.crawler.base.BaseCrawler;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.MySpider;
import com.lavector.collector.crawler.project.douyin.DouYinCrawler;
import us.codecraft.webmagic.Request;

import java.util.Arrays;

/**
 * Created on 2018/4/16.
 *
 * @author zeng.zhao
 */
public class InstagramCrawler extends BaseCrawler {

    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {
//        MySpider spider = MySpider.createUseProxy(new InstagramPageProcessor(crawlerInfo));
        MySpider spider = MySpider.create(new InstagramPageProcessor(crawlerInfo));
        spider.thread(1);
        return spider;
    }

    private void addStartRequests(MySpider spider) {
        Arrays.asList(DouYinCrawler.keywords)
                .forEach(keyword ->
                        spider.addRequest(new Request("https://www.instagram.com/explore/tags/" + keyword + "/?__a=1"))
                );
    }

    public static void main(String[] args) {
        InstagramCrawler crawler = new InstagramCrawler();
        MySpider spider = crawler.createSpider(new CrawlerInfo());
        crawler.addStartRequests(spider);
        spider.start();
    }
}
