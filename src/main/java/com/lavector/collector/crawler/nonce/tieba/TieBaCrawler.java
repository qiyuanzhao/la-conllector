package com.lavector.collector.crawler.nonce.tieba;

import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.project.jd.JDDownloader;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;

/**
 * Created on 23/06/2018.
 *
 * @author zeng.zhao
 */
public class TieBaCrawler {

    private Spider createSpider() {
        Spider spider = Spider.create(new TieBaPageProcessor(new CrawlerInfo()));
        JDDownloader downloader = new JDDownloader();
        spider.setDownloader(downloader);
        spider.thread(10);
        return spider;
    }

    private void start() {
        Spider spider = createSpider();
        spider.addRequest(new Request("http://tieba.baidu.com/f/search/res?isnew=1&kw=&qw=%B7%D2%B4%EF&rn=10&un=&only_thread=1&sm=1&sd=&ed=&pn=1"));
        spider.start();
    }

    public static void main(String[] args) {
        new TieBaCrawler().start();
    }
}
