package com.lavector.collector.crawler.project.sport.nba.hupuSport;

import com.lavector.collector.crawler.base.*;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Request;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 2017/10/16.
 *
 * @author zeng.zhao
 */
@Service(CrawlerType.HUPU_NBA_NEWS)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HuPuSportCrawler extends BaseCrawler {
    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {
        this.setStartTime(DateUtils.addDays(new Date(), -1));
        MySpider spider = MySpider.create(new HuPuSportPageProcessor(crawlerInfo));
        spider.thread(5);
        spider.startRequest(getStartRequest(crawlerInfo));
        return spider;
    }

    private List<Request> getStartRequest (CrawlerInfo crawlerInfo) {
        return crawlerInfo.getUrls().stream().map(url -> {
            Request request = new Request(url);
            request.putExtra(RequestExtraKey.KEY_BEGIN_DATE, this.getStartTime());
            return request;
        }).collect(Collectors.toList());
    }
}
