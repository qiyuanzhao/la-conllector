package com.lavector.collector.crawler.project.game.csgo.pcgames;

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
 * Created on 2017/10/23.
 *
 * @author zeng.zhao
 */
@Service(CrawlerType.CSGO_PCGAMES_EGAME)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PCGamesCrawler extends BaseCrawler {

    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {
        MySpider spider = MySpider.create(new PCGamesPageProcessor(crawlerInfo));
        spider.thread(2);
        spider.startRequest(getStartRequest(crawlerInfo));
        return spider;
    }

    //http://fight.pcgames.com.cn/csgo/news/
    private List<Request> getStartRequest (CrawlerInfo crawlerInfo) {
        return crawlerInfo.getUrls().stream().map(Request::new).collect(Collectors.toList());
    }
}
