package com.lavector.collector.crawler.base;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.nio.charset.StandardCharsets;

/**
 * Created on 10/11/16.
 *
 * @author seveniu
 */
public abstract class BaseProcessor implements PageProcessor {


    private CrawlerInfo crawlerInfo;

    protected PageParse[] pageParses;

    private static String[] USER_AGENTS = new String[]{
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36", // chrome
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.26 Safari/537.36 Core/1.63.5943.400 SLBrowser/10.0.3365.400", // 联想浏览器
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:66.0) Gecko/20100101 Firefox/66.0", //火狐
            "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko" //ie
    };

    public static String randomUserAgent() {
        int user_agent_index = (int) (Math.random() * (USER_AGENTS.length));
        return USER_AGENTS[user_agent_index];
    }


    public BaseProcessor(CrawlerInfo crawlerInfo, PageParse... pageParses) {
        if (pageParses == null || pageParses.length == 0) {
            throw new IllegalArgumentException("page parse size is 0");
        }
        this.pageParses = pageParses;
        if (crawlerInfo == null) {
            throw new IllegalArgumentException("crawler info is null");
        }
        this.crawlerInfo = crawlerInfo;
    }

    @Override
    public void process(Page page) {
        for (PageParse pageParse : pageParses) {
            if (pageParse == null) {
                throw new RuntimeException("page parse is null");
            }
            if (pageParse.handleRequest(page.getRequest())) {
                PageParse.Result result = pageParse.parse(page);
                if (result == null) {
                    return;
                }
                result.getRequests().forEach(page::addTargetRequest);
                page.putField("result", result);
                page.putField("crawlerInfo", crawlerInfo);
                return;
            }
        }
        throw new RuntimeException("un handle url : " + page.getUrl().toString());
    }

    @Override
    public Site getSite() {
        return Site.me()
                .setCycleRetryTimes(3)
                .setTimeOut(10 * 1000)
                .setSleepTime(1000);
    }

    public CrawlerInfo getCrawlerInfo() {
        return crawlerInfo;
    }

    protected String getUserAgent() {
        return "";
    }
}
