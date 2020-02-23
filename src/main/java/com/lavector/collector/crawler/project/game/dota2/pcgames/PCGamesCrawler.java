package com.lavector.collector.crawler.project.game.dota2.pcgames;

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
 * Created on 2017/10/20.
 *
 * @author zeng.zhao
 */
@Service(CrawlerType.DOTA_PCGAMES_EGAME)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PCGamesCrawler extends BaseCrawler {

    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {
        MySpider spider = MySpider.create(new PCGamesPageProcessor(crawlerInfo));
        spider.thread(1);
        spider.startRequest(getStartRequest(crawlerInfo));
        return spider;
    }

    private List<Request> getStartRequest (CrawlerInfo crawlerInfo) {
        return crawlerInfo.getUrls().stream().map(Request::new).collect(Collectors.toList());
    }
}
