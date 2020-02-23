package com.lavector.collector.crawler.project.weibo.weiboAllContent;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.weibo.weiboAllContent.newPage.AjaxFansMessagePage;
import com.lavector.collector.crawler.project.weibo.weiboAllContent.newPage.FansMessageListPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;


public class WeibolPageProcessor implements PageProcessor {

    private Logger logger = LoggerFactory.getLogger(WeibolPageProcessor.class);


    private static Site site = Site.me()
            .setSleepTime(3000)
            .setCycleRetryTimes(3)
            .setCharset("utf-8")
//            .addHeader("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader())
            .addHeader("cookie", "SINAGLOBAL=9262762579760.895.1542259856140; un=13295431080; wvr=6; YF-Page-G0=0acee381afd48776ab7a56bd67c2e7ac; SSOLoginState=1544494256; _s_tentry=login.sina.com.cn; UOR=,,login.sina.com.cn; Apache=727824324915.0607.1544494260952; ULV=1544494260975:7:2:1:727824324915.0607.1544494260952:1543815616137; YF-V5-G0=a53c7b4a43414d07adb73f0238a7972e; Ugrow-G0=968b70b7bcdc28ac97c8130dd353b55e; wb_view_log_5720445059=1920*10801; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhCYUFE6FDNcQ_.N.4UgvQu5JpX5KMhUgL.Fo-Neo5XSh-7SK.2dJLoIEBLxK-L1K5L1-zLxK-L1-zL1--LxKqL1-eL1-zLxKBLBonLBoqt; ALF=1576116657; SCF=AtgnXFBDvRA-ACAFTJbJ_U2hd-M9-tcEg6lbqA47ypoCe4ykN1d4mWYqm8fnphqLoT9YLb3omSPpl8wE9m9A0ZE.; SUB=_2A25xFB5kDeRhGeNJ6VIV9CvMzjWIHXVSYAisrDV8PUNbmtAKLXPkkW9NS_6kL5fwg_oZsZqyh1b5rtOml_Q-H37W; SUHB=0xnCth8oFsyQ_x")
            .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3573.0 Safari/537.36")
            .setTimeOut(15 * 1000);


    @Override
    public void process(Page page) {

        String url = page.getUrl().get();

        if(url.contains("weibo.com/p/aj/v6/mblog/mbloglist?domain")){
            AjaxFansMessagePage ajaxFansMessagePage = new AjaxFansMessagePage();
            PageParse.Result result = ajaxFansMessagePage.parse(page);
            if (result!=null){
                List<Request> requests = result.getRequests();
                for (Request request :requests){
                    page.addTargetRequest(request);
                }
            }
        }else {
            FansMessageListPage fansMessageListPage = new FansMessageListPage();
            PageParse.Result result = fansMessageListPage.parse(page);
            List<Request> requests = result.getRequests();
            for (Request request :requests){
                page.addTargetRequest(request);
            }

        }
    }

    @Override
    public Site getSite() {
        return site;
    }


}
