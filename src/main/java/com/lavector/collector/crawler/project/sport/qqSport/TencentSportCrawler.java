package com.lavector.collector.crawler.project.sport.qqSport;

import com.lavector.collector.crawler.base.BaseCrawler;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.CrawlerType;
import com.lavector.collector.crawler.base.MySpider;
import com.lavector.collector.crawler.util.JsonMapper;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Request;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created on 26/09/2017.
 *
 * @author seveniu
 */
@Service(CrawlerType.QQ_SPORT_TOP)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TencentSportCrawler extends BaseCrawler {
    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {
        MySpider spider = MySpider.create(new TencentSportPageProcessor(crawlerInfo));
        spider.thread(5);
        spider.startRequest(getStartRequests(crawlerInfo));
        return spider;
    }

    private List<Request> getStartRequests(CrawlerInfo crawlerInfo) {
//        return crawlerInfo.getUrls().stream().map(Request::new).collect(Collectors.toList());
        LinkedList<Request> requests = new LinkedList<>();
        requests.add(new Request("http://sports.qq.com/"));
        return requests;
    }

    public static void main(String[] args) {
        CrawlerInfo crawlerInfo = new CrawlerInfo();
        crawlerInfo.setUrls(Collections.singletonList("http://sports.qq.com/"));
        System.out.println(JsonMapper.buildNonNullBinder().toJson(crawlerInfo));
    }
}
