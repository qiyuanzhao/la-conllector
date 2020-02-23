package com.lavector.collector.crawler.project.sport.sinaSport;

import com.lavector.collector.crawler.base.BaseCrawler;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.CrawlerType;
import com.lavector.collector.crawler.base.MySpider;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Request;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 26/09/2017.
 *
 * @author seveniu
 */
@Service(CrawlerType.SINA_SPORT_NEWS)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SinaSportCrawler extends BaseCrawler {
    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {
        MySpider spider = MySpider.create(new SinaSportPageProcessor(crawlerInfo));
        spider.thread(5);
        spider.startRequest(getStartRequests(crawlerInfo));
        return spider;
    }

    private List<Request> getStartRequests(CrawlerInfo crawlerInfo) {
        return crawlerInfo.getUrls().stream().map(Request::new).collect(Collectors.toList());
//        LinkedList<Request> requests = new LinkedList<>();
//        requests.add(new Request("http://news.mtime.com/movie/all/"));
//        return requests;
    }

}
