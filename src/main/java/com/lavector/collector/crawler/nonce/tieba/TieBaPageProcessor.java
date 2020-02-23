package com.lavector.collector.crawler.nonce.tieba;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.nonce.tieba.page.TieBaSearchPage;
import com.lavector.collector.crawler.nonce.tieba.page.TieBaThreadPage;
import us.codecraft.webmagic.Site;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Created on 23/06/2018.
 *
 * @author zeng.zhao
 */
public class TieBaPageProcessor extends BaseProcessor {

    private static LocalDate localDate = LocalDate.of(2018, 3, 20);
    private static ZoneId zoneId = ZoneId.systemDefault();
    private static ZonedDateTime zonedDateTime = localDate.atStartOfDay(zoneId);

    public static Date startTime = Date.from(zonedDateTime.toInstant());

    private Site site = Site.me()
            .setRetrySleepTime(4000)
            .setCycleRetryTimes(5)
            .setTimeOut(15 * 1000)
            .setSleepTime(4000);

    TieBaPageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new TieBaSearchPage(), new TieBaThreadPage());
    }

    @Override
    public Site getSite() {
        return site;
    }
}
