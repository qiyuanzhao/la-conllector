package com.lavector.collector.crawler.project.article.weixin;

import com.lavector.collector.crawler.base.BaseCrawler;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.MySpider;
import com.lavector.collector.thirdpartyapi.weixin.WeiXinApiService;
import com.lavector.collector.thirdpartyapi.weixin.WeiXinApiServiceImpl;
import com.lavector.collector.thirdpartyapi.weixin.entity.ApiEntity;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2018/1/4.
 *
 * @author zeng.zhao
 */
public class WeiXinCrawler extends BaseCrawler {

    private WeiXinApiService weiXinApiService = new WeiXinApiServiceImpl();

    private static Logger logger = LoggerFactory.getLogger(WeiXinCrawler.class);

    private Integer requestCount;

    private List<Request> getStartRequest() {
        List<Request> requests = new ArrayList<>();
        List<WeiXinAccount> accounts = new ArrayList<>();
        if (CollectionUtils.isEmpty(accounts)) {
            logger.warn("微信账号ID为空！");
            return requests;
        }
        ApiEntity apiEntity;
        for(WeiXinAccount account : accounts) {
            int account_pageNo = Integer.parseInt(account.getPageNo());
            apiEntity = weiXinApiService.getArticleEntityList(account.getAccountID(), account_pageNo, account.getPageSize());
            if (apiEntity.getArticleEntities() == null) {
                continue;
            }
            do {
                account_pageNo = Integer.parseInt(account.getPageNo());
                apiEntity.getArticleEntities().forEach(articleEntity -> {
                    Request request = new Request(articleEntity.getContent_url());
                    request.putExtra("articleEntity", articleEntity);
                    requests.add(request);
                });
                sleep();
                if (apiEntity.getLast_page() > account_pageNo) {
                    int nextPageNo = account_pageNo + 1;
                    account.setPageNo(nextPageNo + "");
                    apiEntity = weiXinApiService.getArticleEntityList(account.getAccountID(), nextPageNo, account.getPageSize());
                }
            } while (apiEntity.getLast_page() > account_pageNo);

        }
        requestCount = requests.size();
        return requests;
    }

    private void sleep() {
        try {
            TimeUnit.MILLISECONDS.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected MySpider createSpider(CrawlerInfo crawlerInfo) {
        MySpider spider = MySpider.create(new WeiXinPageProcessor(crawlerInfo));
        spider.startRequest(getStartRequest());
        spider.thread(2);
        return spider;
    }


    @Override
    protected void onFinish() {
        logger.info("{} crawl url size {} .", this.getClass().getSimpleName(), requestCount);
    }

    public static void main(String[] args) {
        WeiXinCrawler crawler = new WeiXinCrawler();
        MySpider spider = crawler.createSpider(new CrawlerInfo());
        spider.start();
    }
}
