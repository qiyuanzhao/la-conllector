package com.lavector.collector.crawler.project.article.toutiao;

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
 * Created on 2017/12/22.
 *
 * @author zeng.zhao
 */
@Service(CrawlerType.TOUTIAO_ARTICLE)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TouTiaoCrawler extends BaseCrawler{

    private static String[] brands = {"GUCCI", "ZARA", "innisfree", "欧舒丹", "SEPHORA", "外婆家",
            "和府捞面", "金吉鸟健身"};

    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {
        MySpider spider = MySpider.create(new TouTiaoPageProcessor(crawlerInfo));
        spider.thread(10);
        spider.startRequest(getStartRequest());
//        spider.setDownloader(new TouTiaoDownloader());
        return spider;
    }

    private List<Request> getStartRequest() {
        List<Request> requests = new ArrayList<>();
        String baseUrl = "https://www.toutiao.com/search_content/?offset=20&format=json&keyword=";
        for (String brand : brands) {
            String url = baseUrl + brand + "&autoload=true&count=20&cur_tab=1&from=search_tab";
            Request request = new Request(url);
            request.putExtra("brand", brand);
            requests.add(request);
        }
        return requests;
    }

    public static void main(String[] args) {
        TouTiaoCrawler crawler = new TouTiaoCrawler();
        MySpider spider = crawler.createSpider(new CrawlerInfo());
        spider.start();
    }
}
