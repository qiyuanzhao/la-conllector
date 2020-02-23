package com.lavector.collector.crawler.project.article.zaker;

import com.lavector.collector.crawler.base.BaseCrawler;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.CrawlerType;
import com.lavector.collector.crawler.base.MySpider;
import com.lavector.collector.crawler.project.article.Brand;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Request;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 2018/1/2.
 *
 * @author zeng.zhao
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Service(CrawlerType.ZAKER_ARTICLE)
public class ZakerCrawler extends BaseCrawler {

    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {
        MySpider spider = MySpider.create(new ZakerPageProcessor(crawlerInfo));
        spider.startRequest(getStartRequest());
        return spider;
    }

    private List<Request> getStartRequest() {
        String baseUrl = "http://www.myzaker.com/news/search_new.php?f=myzaker_com&keyword=";
        return Brand.brands.keySet().stream().map(brand -> {
            Request request = new Request(baseUrl + brand);
            request.putExtra("brand", brand);
            return request;
        }).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        ZakerCrawler crawler = new ZakerCrawler();
        MySpider spider = crawler.createSpider(new CrawlerInfo());
        spider.start();
    }
}
