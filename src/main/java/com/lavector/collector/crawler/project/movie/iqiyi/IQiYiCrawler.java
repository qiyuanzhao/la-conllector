package com.lavector.collector.crawler.project.movie.iqiyi;

import com.google.common.collect.Lists;
import com.lavector.collector.crawler.base.BaseCrawler;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.MySpider;
import com.lavector.collector.crawler.project.food.NewDianpingDownloader;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 2018/1/24.
 *
 * @author zeng.zhao
 */
public class IQiYiCrawler extends BaseCrawler {

    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {
        MySpider spider = MySpider.create(new IQiYiPageProcessor(crawlerInfo));
        HttpClientDownloader downloader = new NewDianpingDownloader();
        downloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("s2.proxy.mayidaili.com", 8123)));
        spider.setDownloader(downloader);
        spider.thread(40);
        return spider;
    }

    private static String[] keywords = {
            "奥迪广告","宝马广告","保时捷广告","北汽广告","奔驰广告","本田广告","比亚迪广告",
            "标致广告","别克广告","长城广告","大众广告","法拉利广告","丰田广告","福特广告",
            "广汽广告","红旗广告","jeep广告","凯迪拉克广告","克莱斯勒广告","兰博基尼广告",
            "劳斯莱斯广告","雷诺广告","马自达广告","欧宝广告","奇瑞广告","起亚广告","日产广告",
            "三菱广告","斯巴鲁广告","斯柯达广告","沃尔沃广告","现代广告","雪佛兰广告","雪铁龙广告",
            "一汽广告"
    };

    private List<Request> getStartRequest() {
        return Arrays.stream(keywords)
                .map(keyword -> new Request("http://so.iqiyi.com/so/q_" + keyword + "?source=input"))
                .collect(Collectors.toList());
    }


    //所有分类链接
    private List<Request> testRequests() {
        return Lists.newArrayList("http://list.iqiyi.com/www/20/-------------11-1-2-iqiyi-1-.html")
                .stream().map(Request::new).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        IQiYiCrawler crawler = new IQiYiCrawler();
        MySpider spider = crawler.createSpider(new CrawlerInfo());
        spider.startRequest(crawler.testRequests());
        spider.start();
    }
}
