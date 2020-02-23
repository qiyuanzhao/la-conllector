package com.lavector.collector.crawler.project.weixinnew;


import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.weixinmall.page.WeixinMallSearchPage;
import com.lavector.collector.crawler.project.weixinnew.page.SearchPage;
import us.codecraft.webmagic.Site;

import java.util.Random;

public class WeixinNewProcesser extends BaseProcessor {


    private static Site site = Site.me()
            .setCycleRetryTimes(3)
            .setCharset("utf-8")
            .addHeader("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader())
            .addHeader("Cookie", "SUID=525BFE2B2213940A000000005BED10F2; SUV=00DD59642BFE5B525BED10F3F045A537; IPLOC=CN1100; pgv_pvi=277137408; CXID=A4F6D1DF8A1DB0A6B94198CFA4E7F951; newsCity=%u5317%u4EAC; ld=Syllllllll2N1fhrlllllVTU$scllllltsoYByllll9lllllVllll5@@@@@@@@@@; ad=zFy8vyllll2N1fhHlllllVTU$ZlllllltsoYByllll9lllllVllll5@@@@@@@@@@; LSTMV=472%2C70; LCLKINT=2198; sortcookie=1; PHPSESSID=stf2dlqb6fm66t1pfkp1drh793; SNUID=BCC022AAD8DD4F1AC1F5CFB9D99680CD; seccodeRight=success; successCount=1|Wed, 20 Nov 2019 07:31:08 GMT; sct=121")
            .addHeader("Referer","https://news.sogou.com/news?ie=utf8&p=40230447&interV=kKIOkrELjboMmLkEkLYTkKIMkLELjbgQmLkElbcTkKIMkbELjb8TkKILmrELjbgRmUHpGz2IOzXejb0Ew+dByOsG0OV/zPsGwOVFmUHpGHIElKJLzO5Nj+lHzrGIOzzgjb0Ew+dBxfsGwOVF_506047069&query=%E8%B5%84%E7%94%9F%E5%A0%82&")
            .setTimeOut(10 * 1000);

    public WeixinNewProcesser(CrawlerInfo crawlerInfo, PageParse... pageParses) {
        super(crawlerInfo, new SearchPage());
    }


    @Override
    public Site getSite() {
        Random random = new Random();
        int time = random.nextInt(10000);
        return site.setSleepTime(3000 + time);
    }

}
