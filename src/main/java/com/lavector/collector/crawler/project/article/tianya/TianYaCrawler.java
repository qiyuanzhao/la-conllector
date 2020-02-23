package com.lavector.collector.crawler.project.article.tianya;

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
@Service(CrawlerType.TIANYA_ARTICLE)
public class TianYaCrawler extends BaseCrawler {

    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {
        MySpider spider = MySpider.create(new TianYaPageProcessor(crawlerInfo));
        spider.thread(10);
        spider.startRequest(getStartRequest());
        return spider;
    }

    private List<Request> getStartRequest() {
        String baseUrl = "http://search.tianya.cn/bbs?q=";
        return Brand.brands.keySet().stream().map(brand -> {
            Request request = new Request(baseUrl + brand + "&pn=1&s=4");
            request.putExtra("brand", brand);
            return request;
        }).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        TianYaCrawler crawler = new TianYaCrawler();
        MySpider spider = crawler.createSpider(new CrawlerInfo());
        spider.start();
    }
}
