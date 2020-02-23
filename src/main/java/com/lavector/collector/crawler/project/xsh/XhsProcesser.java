package com.lavector.collector.crawler.project.xsh;


import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.xsh.page.DetilePage;
import com.lavector.collector.crawler.project.xsh.page.SearchPage;
import us.codecraft.webmagic.Site;

import java.util.Random;

public class XhsProcesser extends BaseProcessor {


    private static Site site = Site.me()
            .setCycleRetryTimes(3)
            .setCharset("utf-8")
//            .addHeader("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader())
//            .addHeader("Cookie", "SINAGLOBAL=9262762579760.895.1542259856140; UOR=,,login.sina.com.cn; login_sid_t=e65431f5c3f398f534f1f531958b977e; cross_origin_proto=SSL; _s_tentry=passport.weibo.com; Apache=8549128340925.876.1557382099881; ULV=1557382099893:8:1:1:8549128340925.876.1557382099881:1555922206786; SCF=AtgnXFBDvRA-ACAFTJbJ_U2hd-M9-tcEg6lbqA47ypoCyZ0GQISuQ2Pa74vuawJcO0WEY1tlN_Q0ntEwEOjQQgM.; SUHB=0rr7yEJilfAWXC; webim_unReadCount=%7B%22time%22%3A1557382146246%2C%22dm_pub_total%22%3A0%2C%22chat_group_pc%22%3A0%2C%22allcountNum%22%3A1%2C%22msgbox%22%3A0%7D; SUBP=0033WrSXqPxfM72wWs9jqgMF55529P9D9WhCYUFE6FDNcQ_.N.4UgvQu5JpVF020eo.fShepehn7; SUB=_2AkMrj0sydcPxrAZWmvgRyG7iaYRH-jyYWiLEAn7uJhMyAxgv7kgGqSVutBF-XHifvRRkWncFHRwnvEcf61FLat9b")
            .addHeader("Cookie","xhsTrackerId=e0b53a22-360f-45a9-c594-11cfa1a933c9; xhsuid=MOrkzBS5Pq2XHfV9; Hm_lvt_9df7d19786b04345ae62033bd17f6278=1563771411,1563771419; Hm_lvt_d0ae755ac51e3c5ff9b1596b0c09c826=1563771411,1563771419; smidV2=201910151415598d23d4378daa6291bacd79a2a0b300a7002de6e484f02cf90; extra_exp_ids=; timestamp1=3748385669; hasaki=JTVCJTIyTW96aWxsYSUyRjUuMCUyMChXaW5kb3dzJTIwTlQlMjAxMC4wJTNCJTIwV2luNjQlM0IlMjB4NjQpJTIwQXBwbGVXZWJLaXQlMkY1MzcuMzYlMjAoS0hUTUwlMkMlMjBsaWtlJTIwR2Vja28pJTIwQ2hyb21lJTJGNzkuMC4zOTQ1Ljg4JTIwU2FmYXJpJTJGNTM3LjM2JTIyJTJDJTIyemgtQ04lMjIlMkMyNCUyQy00ODAlMkN0cnVlJTJDdHJ1ZSUyQ3RydWUlMkMlMjJ1bmRlZmluZWQlMjIlMkMlMjJmdW5jdGlvbiUyMiUyQ251bGwlMkMlMjJXaW4zMiUyMiUyQzQlMkM4JTJDbnVsbCUyQyUyMkNocm9tZSUyMFBERiUyMFBsdWdpbiUzQSUzQVBvcnRhYmxlJTIwRG9jdW1lbnQlMjBGb3JtYXQlM0ElM0FhcHBsaWNhdGlvbiUyRngtZ29vZ2xlLWNocm9tZS1wZGZ+cGRmJTNCQ2hyb21lJTIwUERGJTIwVmlld2VyJTNBJTNBJTNBJTNBYXBwbGljYXRpb24lMkZwZGZ+cGRmJTNCTmF0aXZlJTIwQ2xpZW50JTNBJTNBJTNBJTNBYXBwbGljYXRpb24lMkZ4LW5hY2x+JTJDYXBwbGljYXRpb24lMkZ4LXBuYWNsfiUyMiUyQzIzMDg5NjEyNzAlNUQ=; timestamp2=3e3635fef535403668e19d7762332d13; xhs_spses.5dde=*; xhs_spid.5dde=53fd119d9948bb49.1547548827.39.1578385274.1577948932.d66cb276-f16f-4a47-a4c9-ac98321e0027")
            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:70.0) Gecko/20100101 Firefox/70.0")
//            .addHeader("Referer","https://www.baidu.com/")
//            .addHeader("Host","weibo.com")
//            .addHeader("Connection","keep-alive")
//            .addHeader("Cache-Control","max-age=0")
//            .addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
//            .addHeader("Upgrade-Insecure-Requests","1")
//            .addHeader("Accept-Encoding","gzip, deflate, br")
//            .addHeader("Accept-Language","zh-CN,zh;q=0.9")
//            .addHeader("Cache-Control","max-age=0")
            .setTimeOut(10 * 1000);

    public XhsProcesser(CrawlerInfo crawlerInfo, PageParse... pageParses) {
        super(crawlerInfo, new SearchPage(),new DetilePage());
    }


    @Override
    public Site getSite() {
        Random random = new Random();
        int time = random.nextInt(8000);
        return site.setSleepTime(5000);
    }



}
