package com.lavector.collector.crawler.project.article.yidian;

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
 * Created on 2017/12/27.
 *
 * @author zeng.zhao
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Service(CrawlerType.YIDIANZIXUN_ARTICLE)
public class YiDianCrawler extends BaseCrawler {

    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {
        MySpider spider = MySpider.create(new YiDianPageProcessor(crawlerInfo));
        spider.thread(5);
        spider.startRequest(getStartRequest());
        return spider;
    }

    private List<Request> getStartRequest() {
        String baseUrl = "http://www.yidianzixun.com/home/q/news_list_for_keyword?display=";
        return Brand.brands.keySet().stream().map(brand -> {
            Request request = new Request(baseUrl + brand + "&cstart=0&cend=10&word_type=token");
            request.putExtra("brand", brand);
            return request;
        }).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        YiDianCrawler crawler = new YiDianCrawler();
        MySpider spider = crawler.createSpider(new CrawlerInfo());
        spider.start();
    }
}
