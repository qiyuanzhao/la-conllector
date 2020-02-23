package com.lavector.collector.crawler.project.article.huxiu;

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
 * Created on 2017/12/26.
 *
 * @author zeng.zhao
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Service(CrawlerType.HUXIU_ARTICLE)
public class HuXiuCrawler extends BaseCrawler {

    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {
        MySpider spider = MySpider.create(new HuXiuPageProcessor(crawlerInfo));
        spider.thread(5);
        spider.startRequest(getStartRequest());
        return spider;
    }

    private List<Request> getStartRequest() {
        String baseUrl = "https://www.huxiu.com/search.html?s=";
        return Brand.brands.keySet().stream().map(brand -> {
            Request request = new Request(baseUrl + brand);
            request.putExtra("brand", brand);
            return request;
        }).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        HuXiuCrawler crawler = new HuXiuCrawler();
        MySpider spider = crawler.createSpider(new CrawlerInfo());
        spider.start();
    }
}
