package com.lavector.collector.crawler.project.edu.mama;

import com.lavector.collector.crawler.base.BaseCrawler;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.MySpider;
import com.lavector.collector.crawler.project.edu.Keywords;
import us.codecraft.webmagic.Request;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/11/16.
 *
 * @author zeng.zhao
 */
public class MaMaCrawler extends BaseCrawler {

    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {
        MySpider spider = MySpider.create(new MaMaPageProcessor(crawlerInfo));
        spider.thread(10);
        return spider;
    }

    private List<Request> getStartRequest () {
        List<Request> requests = new ArrayList<>();
        for (String keyword : Keywords.keywords) {
            Request request = new Request("http://so.mama.cn/search?q=" + keyword + "&source=mamaquan&csite=all&size=15&sortMode=1&page=1");
            request.putExtra("keyword", keyword).putExtra("site", "妈妈网");
            requests.add(request);
        }
        return requests;
    }

    public static void main (String[] args) {
        MaMaCrawler crawler = new MaMaCrawler();
        MySpider spider = crawler.createSpider(new CrawlerInfo());
        spider.startRequest(crawler.getStartRequest());
        spider.start();
    }
}
