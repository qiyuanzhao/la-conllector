package com.lavector.collector.crawler.project.edu.zhihu;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.project.edu.zhihu.page.AnswerListPage;
import com.lavector.collector.crawler.project.edu.zhihu.page.QuestionListPage;
import us.codecraft.webmagic.Site;

/**
 * Created on 2017/11/20.
 *
 * @author zeng.zhao
 */
public class ZhiHuPageProcessor extends BaseProcessor {

    private Site site = Site.me()
            .setTimeOut(30 * 1000)
            .setCycleRetryTimes(3)
            .setRetrySleepTime(1500)
            .setSleepTime(1500)
            .addHeader("authorization", "oauth c3cef7c66a1843f8b3a9e6a1e3160e20")
            .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36")
            ;

    public ZhiHuPageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new QuestionListPage(), new AnswerListPage());
    }

    @Override
    public Site getSite() {
        return site;
    }
}
