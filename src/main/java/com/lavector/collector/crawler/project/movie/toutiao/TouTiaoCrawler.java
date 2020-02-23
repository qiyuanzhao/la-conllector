package com.lavector.collector.crawler.project.movie.toutiao;

import com.lavector.collector.crawler.base.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 2017/10/25.
 *
 * @author zeng.zhao
 */
@Service(CrawlerType.TOUTIAO_MOVIE)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TouTiaoCrawler extends BaseCrawler {
    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {
        MySpider spider = MySpider.create(new TouTiaoPageProcessor(crawlerInfo));
        spider.thread(2);
        spider.startRequest(getStartRequests());
        return spider;
    }

    //http://www.toutiao.com/api/pc/feed/?category=movie&utm_source=toutiao&widen=1&max_behot_time=0&max_behot_time_tmp=0&tadrequire=true
    private List<Request> getStartRequests() {
        List<Request> requests = new ArrayList<>();
        String basicUrl = "http://www.toutiao.com/api/pc/feed/?category=movie&utm_source=toutiao&widen=1&max_behot_time=0&max_behot_time_tmp=0&tadrequire=true";
        Request request = new Request(basicUrl);
        request.putExtra(RequestExtraKey.KEY_BEGIN_DATE, getStartTime());
        requests.add(request);
        return requests;
    }
}
