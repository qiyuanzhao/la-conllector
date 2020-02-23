package com.lavector.collector.crawler.nonce.dianping;

import com.google.common.io.CharSink;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

/**
 * Created on 11/05/2018.
 *
 * @author zeng.zhao
 */
public class DianPingPageProcessor extends BaseProcessor {

    private static CharSink charSink = Files.asCharSink(Paths.get("/Users/zeng.zhao/Desktop/dianping_message_person.json").toFile(),
            StandardCharsets.UTF_8, FileWriteMode.APPEND);

    private Site site = Site.me()
            .setCharset("utf-8")
            .setRetrySleepTime(2000)
            .setCycleRetryTimes(3)
            .setTimeOut(15 * 1000)
            .setSleepTime(2000);

    DianPingPageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo,
                new DianPingSearchPage(),
                new DianPingHostPage(),
                new DianPingCommentListPage(),
                new DianPingPersonCommentPage(),
                new DianPingPersonCheckinPage(),
                new DianPingShopPage());
    }


    @Override
    public Site getSite() {
//        site.addHeader("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader());
//        site.addHeader("User-Agent", this.getUserAgent());
        return site;
    }

    public static synchronized void write(String s) {
        try {
            charSink.write(s + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
