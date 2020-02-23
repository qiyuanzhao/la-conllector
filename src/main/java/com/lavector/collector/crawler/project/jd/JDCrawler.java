package com.lavector.collector.crawler.project.jd;

import com.lavector.collector.crawler.base.BaseCrawler;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.MySpider;
import us.codecraft.webmagic.Request;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 2018/1/17.
 *
 * @author zeng.zhao
 */
public class JDCrawler extends BaseCrawler {

    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {
        MySpider spider = MySpider.create(new JDPageProcessor(crawlerInfo));
        spider.thread(1);
        return spider;
    }

    private String[] itemIds = new String[]{
            "4870826", "3773207"
    };


    //https://sclub.jd.com/comment/productPageComments.action?callback=&productId=1591434542&score=0&sortType=6&page=0&pageSize=10&isShadowSku=0&fold=1
    private List<Request> getStartRequestByProductID() {
        return Arrays.stream(itemIds).map(itemId -> {
            Request request = new Request("https://item.jd.com/" + itemId + ".html");
            request.putExtra("itemId", itemId);
            return request;
        }).collect(Collectors.toList());
    }


    private String[] keywords = new String[]{
            "稻香村"
    };

    private List<Request> getStartRequestByKeyword() {
        return Arrays.stream(keywords).map(keyword -> {
            Request request = new Request("https://search.jd.com/Search?keyword=" + keyword + "&enc=utf-8&page=1");
            request.putExtra("keyword", keyword);
            return request;
        }).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        JDCrawler crawler = new JDCrawler();
        MySpider spider = crawler.createSpider(new CrawlerInfo());
        spider.thread(5);
        spider.setDownloader(new JDDownloader());
//        spider.startRequest(crawler.getStartRequestByProductID());
        spider.addRequest(new Request("https://search.jd.com/search?keyword=%E6%9E%9C%E9%86%8B&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&suggest=1.rem.0.V02&wq=%E6%9E%9C%E9%86%8B&cid2=1585&cid3=1602&stock=1&page=1&s=1&click=0")
                .putExtra("keyword", "果醋"));
        spider.addRequest(new Request("https://item.jd.com/5123256.html")
                .putExtra("itemId", "5123256"));
        spider.start();
    }

}
