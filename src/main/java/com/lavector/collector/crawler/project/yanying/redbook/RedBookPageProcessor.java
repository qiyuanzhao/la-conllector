package com.lavector.collector.crawler.project.yanying.redbook;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import com.lavector.collector.crawler.project.yanying.redbook.page.CommentPage;
import com.lavector.collector.crawler.project.yanying.redbook.page.ItemPage;
import com.lavector.collector.crawler.project.yanying.redbook.page.SearchPage;
import com.lavector.collector.crawler.project.yanying.redbook.page.SubCommentPage;
import us.codecraft.webmagic.Site;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Created on 2018/2/6.
 *
 * @author zeng.zhao
 */
public class RedBookPageProcessor extends BaseProcessor {

    private static ZoneId zoneId = ZoneId.systemDefault();

    private static LocalDate localDate = LocalDate.of(2018, 3, 20);

    private static ZonedDateTime zonedDateTime = localDate.atStartOfDay(zoneId);

    public static Date startTime = Date.from(zonedDateTime.toInstant());

    private Site site = Site.me()
            .setRetrySleepTime(4000)
            .setCycleRetryTimes(5)
            .setTimeOut(15 * 1000)
            .setSleepTime(4000)
//            .addHeader("host", "www.xiaohongshu.com")
//            .addHeader("cookie", "xhsTrackerId=98a082f9-e1b6-4e49-ce5e-45d3b4897c22; Max-Age=1892160000; Domain=.xiaohongshu.com;")
            .setCharset("utf-8");

    RedBookPageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new SearchPage(), new ItemPage(), new CommentPage(), new SubCommentPage());
    }

    @Override
    public Site getSite() {
//        site.addHeader("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader());
//        site.addHeader("user-agent", getUserAgent());
        return site;
    }
}
