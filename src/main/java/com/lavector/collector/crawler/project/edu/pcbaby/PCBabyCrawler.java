package com.lavector.collector.crawler.project.edu.pcbaby;

import com.lavector.collector.crawler.base.BaseCrawler;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.MySpider;
import com.lavector.collector.crawler.project.edu.Keywords;
import com.lavector.collector.crawler.project.food.DianpingDownloader;
import com.lavector.collector.crawler.util.UrlUtils;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/11/15.
 *
 * @author zeng.zhao
 */
public class PCBabyCrawler extends BaseCrawler {

    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {
        MySpider spider = MySpider.create(new PCBabyPageProcessor(crawlerInfo));
        spider.thread(5);
        return spider;
    }

    private List<Request> getStartRequest() {
        List<Request> requests = new ArrayList<>();
        String url = "http://ks.pcbaby.com.cn/kids_bbs.shtml?q=";
        try {
            for (String keyword : Keywords.keywords) {
                Request request = new Request(url + URLEncoder.encode(keyword, "gb2312") + "&sort=time&pageNo=1")
                        .putExtra("keyword", keyword)
                        .putExtra("site", "太平洋亲子网");
                requests.add(request);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public static void main (String[] args) throws Exception {
        PCBabyCrawler crawler = new PCBabyCrawler();
        MySpider spider = crawler.createSpider(new CrawlerInfo());
        spider.startRequest(crawler.getStartRequest());
        spider.start();
//        System.out.println(URLEncoder.encode("学区", "gb2312"));
    }
}
