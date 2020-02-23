package com.lavector.collector.crawler.project.sport.ufc.cn;

import com.lavector.collector.crawler.base.BaseCrawler;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.CrawlerType;
import com.lavector.collector.crawler.base.MySpider;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Request;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/10/19.
 *
 * @author zeng.zhao
 */
@Service(CrawlerType.UFC_SPORT)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UFCCrawler extends BaseCrawler {

    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {
        MySpider spider = MySpider.create(new UFCPageProcessor(crawlerInfo));
        spider.thread(1);
        spider.startRequest(getStartRequest());
        return spider;
    }

    private List<Request> getStartRequest () {
        List<Request> requests = new ArrayList<>();
        requests.add(new Request("http://interface.sina.cn/sports/get_ufc_news.d.json?pageNum=1&pageSize=16"));
        return requests;
    }
}
