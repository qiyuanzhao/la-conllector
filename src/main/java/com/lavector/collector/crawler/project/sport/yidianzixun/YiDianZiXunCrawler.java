package com.lavector.collector.crawler.project.sport.yidianzixun;

import com.lavector.collector.crawler.base.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Request;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/10/17.
 *
 * @author zeng.zhao
 */
@Service(CrawlerType.YIDIANZIXUN_SPORT_NEWS)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class YiDianZiXunCrawler extends BaseCrawler {

    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {
        MySpider spider = MySpider.create(new YiDianZiXunPageProcessor(crawlerInfo));
        spider.thread(2);
        spider.startRequest(getStartRequest());
        return spider;
    }

    /**
     * start url : json
     * @return requests
     */
    private List<Request> getStartRequest () {
        String startUrl = "http://www.yidianzixun.com/home/q/news_list_for_channel?channel_id=sc4&cstart=20&cend=30&infinite=true&refresh=1&__from__=pc&multi=5&appid=web_yidian";
        List<Request> list = new ArrayList<>();
        list.add(new Request(startUrl).putExtra(RequestExtraKey.KEY_BEGIN_DATE, getStartTime()));
        return list;
    }
}
