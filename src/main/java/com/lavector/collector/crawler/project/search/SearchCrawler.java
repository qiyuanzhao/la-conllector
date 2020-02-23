package com.lavector.collector.crawler.project.search;

import com.lavector.collector.crawler.base.BaseCrawler;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.MySpider;
import com.lavector.collector.crawler.project.food.DianpingDownloader;
import org.apache.commons.lang3.time.DateUtils;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created on 2017/11/30.
 *
 * @author zeng.zhao
 */
public class SearchCrawler extends BaseCrawler {

    public static final Date targetDate = DateUtils.addDays(new Date(), -90);

    private List<Request> startRequests () throws Exception {
        String keywordStr = "奥迪 A5 售价,奥迪 A5 内饰,奥迪 A5 动力,奥迪 A5 油耗,奥迪 A5 外观,奥迪 A5 空间,奥迪 A5 销量,奥迪 A5 发动机,奥迪 A5 价格,奥迪 A5 性价比,奥迪 A5 实拍,奥迪 A5 上市,奥迪 A5 安全,奥迪 A5 颜值,奥迪 A5 安全性能,奥迪 A5 舒适,奥迪 A5 感受,奥迪 A5 推荐,奥迪 A5 造型,奥迪 A5 颜色,奥迪 A5 灯,奥迪 A5 轮毂,奥迪 A5 加速,奥迪 A5 变速箱,奥迪 A5 MIMI,奥迪 A5 座舱,奥迪 A5 尺寸,奥迪 A5 落地价,奥迪 A5 裸车价,奥迪 A5 优惠,奥迪 A5 配置,奥迪A5,奥迪a5";
        String[] keywords = keywordStr.split(",");
        List<Request> requests = new ArrayList<>();
        for (String keyword : keywords) {
            Request request = new Request("https://sou.autohome.com.cn/luntan?q=" + URLEncoder.encode(keyword, "gbk") + "&pvareaid=100834&entry=44&clubClassBefore=&IsSelect=0&clubOrder=Relevance&clubClass=&clubSearchType=&clubSearchTime=&pq=%B0%C2%B5%CF+A5+%B0%B2%C8%AB%D0%D4%C4%DC&pt=636476632367712039");
            requests.add(request);
        }

//        requests.add(new Request("https://sou.autohome.com.cn/luntan?page=2&pvareaid=100834&pt=636476632367712039&clubOrder=Relevance&q=%B0%C2%B5%CF+A5+%CD%E2%B9%DB&pq=%B0%C2%B5%CF+A5+%B0%B2%C8%AB%D0%D4%C4%DC&entry=44&IsSelect=0&page=40"));
        return requests;
    }

    public static void main (String[] args) throws Exception {
        SearchCrawler crawler = new SearchCrawler();
        Spider spider = crawler.createSpider(new CrawlerInfo());
        spider.startRequest(crawler.startRequests());
        spider.setDownloader(new AutoHomeDownloader());
        spider.start();

        //        List<Request> requests = crawler.startRequests();
//        for (int i = 0; i < requests.size(); i++) {
//            String content = org.apache.http.client.fluent.Request.Get(requests.get(i).getUrl())
//                    .execute()
//                    .returnContent()
//                    .asString();
//            System.out.println(content);
//        }
    }

    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {
        MySpider spider = MySpider.create(new SearchPageProcessor(crawlerInfo));
        spider.thread(5);
        return spider;
    }
}
