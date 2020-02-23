package com.lavector.collector.crawler.project.taobao;


import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import com.lavector.collector.crawler.project.taobao.page.CommentPage;
import com.lavector.collector.crawler.project.taobao.page.SearchPage;
import us.codecraft.webmagic.Site;

public class BabyTreeProcessor extends BaseProcessor {

    private Site site = Site.me()
            .addHeader("User-Agent", randomUserAgent())
            .addHeader("Cookie","thw=cn; hng=CN%7Czh-CN%7CCNY%7C156; _uab_collina=154270641149284646362814; x=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0%26__ll%3D-1%26_ato%3D0; t=3d2ddf1a611d4b6ad4b94275966f138e; enc=JZ9CJvydKPduBpqtdalkAhQUD43N9p0De5DwnHNowwZjDXzdTLWnXLufHF2dGw9trxZ%2FI0xYRdSWeQWuMszTXw%3D%3D; tk_trace=oTRxOWSBNwn9dPyorMJE%2FoPdY8zfvmw%2Fq5hnQ6wxshYRSngzzSUPb0vnoncbChW66idFi6F%2FILthVKUi7nZ89xQc1mMaZ2kYIa5sOqGdPRToraR81p2rwx3cij1Zj%2FbIwH8iYDygLjOPsP%2FteDFx1oM49oYtZAzUe1SMvO%2Bi3xUVM4rzkLws98QRa2b4xSTyYrcTOSppFz1RzpCIwcf4zopm25os175hZUCCmxxg6ifHXAX6e4lVsyl%2B9AZhUgZ9K0wwo5IQxdDyf66yniwWvEoijw%3D%3D; cookie2=14d378c755f7a7ca348aeb1239490d09; _tb_token_=e365beeef3178; mt=ci=0_0; v=0; cna=6KZmFH+sOWkCAXjAWe1mcgAH; alitrackid=www.taobao.com; lastalitrackid=www.taobao.com; JSESSIONID=30DAFEAA571E1EF3D356626FF36B0369; l=cB_2HuXPqyCAYwGsBOCZourza77TjIRAguPzaNbMi_5aZ6L1rjbOk6TI_Fp6cjWdt1TB4JuaUMv9-etkiKy06Pt-g3fP.; isg=BDIyaGPXu8DosIceU1TTmc1lg3gUwzZdS6NvDfwLU-XQj9KJ5FPebwvte2uW_671")
//            .addHeader("upgrade-insecure-requests","1")
//            .addHeader("cache-control","max-age=0")
//            .addHeader("accept-language","zh-CN,zh;q=0.9")
//            .addHeader("accept-encoding","gzip, deflate, br")
            .addHeader("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader())
//            .addHeader("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
//            .addHeader("accept","*/*")
////            .addHeader(":authority","s.taobao.com")
//            .addHeader(":authority","rate.taobao.com")
//            .addHeader(":method","GET")

            //.addHeader(":path","/search?q=%E5%84%BF%E7%AB%A5%E8%90%A5%E5%85%BB%E5%93%81&imgfile=&js=1&stats_click=search_radio_all%3A1&initiative_id=staobaoz_20190115&ie=utf8&bcoffset=6&ntoffset=6&p4ppushleft=1%2C48&s=0")
//            .addHeader(":scheme","https")
            .setRetrySleepTime(5000)
            .setSleepTime(1000)
            .setCycleRetryTimes(5);

    public BabyTreeProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo,new SearchPage(),new CommentPage());
    }


    @Override
    public Site getSite() {
        return site;
    }


    private static String[] USER_AGENTS = new String[]{
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36", // chrome
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/604.3.5 (KHTML, like Gecko) Version/11.0.1 Safari/604.3.5", // safari
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36", // 360
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:45.0) Gecko/20100101 Firefox/45.0"
    };


    public static String randomUserAgent() {
        int user_agent_index = (int) (Math.random() * (USER_AGENTS.length));
        return USER_AGENTS[user_agent_index];
    }
}
