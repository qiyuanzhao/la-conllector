package com.lavector.collector.crawler.project.yanying.redbook;

import com.google.common.collect.Lists;
import com.lavector.collector.crawler.base.BaseCrawler;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.MySpider;
import com.lavector.collector.crawler.project.food.NewDianpingDownloader;
import com.lavector.collector.crawler.project.jd.JDDownloader;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

import java.util.List;

/**
 * Created on 2018/2/6.
 *
 * @author zeng.zhao
 */
public class RedBookCrawler extends BaseCrawler {

    public static final String OUT_FILE = "/Users/zeng.zhao/Desktop/red_book(芬达).json";

    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {
        MySpider spider = MySpider.create(new RedBookPageProcessor(crawlerInfo));
        spider.thread(10);
        return spider;
    }

    private List<Request> getStartRequest() {
        return Lists.newArrayList(
//                new Request("http://www.xiaohongshu.com/web_api/sns/v2/search/note?keyword=%E7%BE%8E%E5%B9%B4%E8%BE%BE&page=1")
//                .putExtra("keyword", "美年达")
                new Request("http://www.xiaohongshu.com/web_api/sns/v2/search/note?keyword=%E8%8A%AC%E8%BE%BE&page=60")
                .putExtra("keyword", "芬达")
        );
    }

    private List<Request> testRequests() {
        // http://www.xiaohongshu.com/discovery/item/5a768b020b14a7654dad88a1
        return Lists.newArrayList(new Request("http://www.xiaohongshu.com/discovery/item/5a768b020b14a7654dad88a1"));
    }

    public static void main(String[] args) {
        RedBookCrawler crawler = new RedBookCrawler();
        MySpider spider = crawler.createSpider(new CrawlerInfo());
        JDDownloader downloader = new JDDownloader();
        spider.setDownloader(downloader);
        spider.startRequest(crawler.getStartRequest());
        spider.start();
    }
}
