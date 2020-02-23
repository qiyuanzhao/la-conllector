package com.lavector.collector.crawler.project.edu.zhihu;

import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import com.lavector.collector.crawler.project.edu.Keywords;
import com.lavector.collector.crawler.project.food.DianpingDownloader;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/11/20.
 *
 * @author zeng.zhao
 */
public class ZhiHuCrawler {


    private List<Request> getStartRequests () throws Exception {
        List<Request> requests = new ArrayList<>();
        for (String keyword : Keywords.keywords) {
            String url = "https://www.zhihu.com/r/search?q=" + URLEncoder.encode(keyword, "utf-8")
                    + "&correction=1&type=content&offset=10";
            Request request = new Request(url);
            request.putExtra("site", "知乎");
            request.putExtra("keyword", keyword);
            requests.add(request);
        }
        return requests;
    }

    public static void main (String[] args) throws Exception {
        ZhiHuCrawler crawler = new ZhiHuCrawler();
        Spider spider = Spider.create(new ZhiHuPageProcessor(new CrawlerInfo()));
        spider.thread(10);
        spider.startRequest(crawler.getStartRequests());
        spider.start();
    }
}
