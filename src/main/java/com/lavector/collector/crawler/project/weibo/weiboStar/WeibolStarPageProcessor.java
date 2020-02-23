package com.lavector.collector.crawler.project.weibo.weiboStar;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.weibo.weiboStar.page.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;
import java.util.Random;


public class WeibolStarPageProcessor implements PageProcessor {

    private Logger logger = LoggerFactory.getLogger(WeibolStarPageProcessor.class);


    private static Site site = Site.me()
            .setCycleRetryTimes(3)
            .setCharset("utf-8")
//            .addHeader("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader())
            .addHeader("cookie", "SINAGLOBAL=9262762579760.895.1542259856140; UOR=,,login.sina.com.cn; wb_view_log_6689991112=1920*10801; YF-V5-G0=590253f9bb559fcb4f19c58020522401; Ugrow-G0=5b31332af1361e117ff29bb32e4d8439; YF-Page-G0=074bd03ae4e08433ef66c71c2777fd84; login_sid_t=b24873b79c593ffae46cfb46ef41ce50; cross_origin_proto=SSL; WBStorage=d4fb937c8bb4f57c|undefined; wb_view_log=1920*10801%261366*7681; _s_tentry=login.sina.com.cn; Apache=1996211833015.6042.1550845889046; ULV=1550845889053:2:2:2:1996211833015.6042.1550845889046:1550564409833; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhCYUFE6FDNcQ_.N.4UgvQu5JpX5K2hUgL.Fo-Neo5XSh-7SK.2dJLoIEXLxK-LBo5L12qLxK.LBKzL1KqLxKML1-2L1hBLxK-L1KeLBK-LxK-L1KeLBK-t; ALF=1582382017; SSOLoginState=1550846017; SCF=AtgnXFBDvRA-ACAFTJbJ_U2hd-M9-tcEg6lbqA47ypoCoJrtT07KCJEsSNAoD4-BgYDQKZlgXYzLafvTEmK1RhE.; SUB=_2A25xdHgRDeRhGeNJ6VIV9CvMzjWIHXVSAO7ZrDV8PUNbmtANLRfGkW9NS_6kLykBy_CaKICi_6jk_tQdLY0NAX85; SUHB=0V5lDtJ5nJ5Fpi; un=13295431080; wvr=6; wb_view_log_5720445059=1366*7681; webim_unReadCount=%7B%22time%22%3A1550846053262%2C%22dm_pub_total%22%3A0%2C%22chat_group_pc%22%3A0%2C%22allcountNum%22%3A0%2C%22msgbox%22%3A0%7D")
            .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3573.0 Safari/537.36")
            .setTimeOut(15 * 1000);


    @Override
    public void process(Page page) {

        String url = page.getUrl().get();
        if (url.matches("https://weibo.com/aj/v6/comment/big\\?ajwvr=6&id=[0-9]+&from=singleWeiBo")) {
            WeiboDetilPage weiboDetilPage = new WeiboDetilPage();
            PageParse.Result result = weiboDetilPage.parse(page);
            checkResult(result, page);
        } else if (url.matches("https://weibo.com/aj/v6/comment/big\\?from=singleWeiBo&id=[0-9]+&page=[0-9]+")) {
            WeiboAjaxCommentPage weiboAjaxCommentPage = new WeiboAjaxCommentPage();
            weiboAjaxCommentPage.parse(page);
        } else if (url.matches("https://weibo.com/[0-9 a-z]+")) {
            UserPage userPage = new UserPage();
            userPage.parse(page);
        } else if (url.matches("https://weibo.com/p/[0-9]+/info\\?mod=pedit_more")) {
            UserDetilPage userDetilPage = new UserDetilPage();
            userDetilPage.parse(page);
        } else if (url.contains("?is_all=1")) {
            WeiboSearchPage weiboSearchPage = new WeiboSearchPage();
            PageParse.Result result = weiboSearchPage.parse(page);
            checkResult(result, page);
        }
    }

    private void checkResult(PageParse.Result result, Page page) {
        if (result.getRequests() != null) {
            List<Request> requests = result.getRequests();
            for (Request request : requests) {
                page.addTargetRequest(request);
            }
        }
    }

    @Override
    public Site getSite() {
        Random random = new Random();
        int time = random.nextInt(5) + 10;
        return site.setSleepTime(time*1000);
    }


}
