package com.lavector.collector.crawler.project.wqc;


import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.weibo.weiboPerson.page.PersonDetilPage;
import com.lavector.collector.crawler.project.weibo.weiboPerson.page.PersonPage;
import com.lavector.collector.crawler.project.wqc.page.MainPage;
import com.lavector.collector.crawler.project.wqc.page.SecendPage;
import com.lavector.collector.crawler.project.wqc.page.ThreePage;
import com.lavector.collector.crawler.project.wqc.page.WqcDetilePage;
import com.lavector.collector.crawler.project.wqc.page.WqcSearchPage;
import us.codecraft.webmagic.Site;

import java.util.Random;

public class WqcProcesser extends BaseProcessor {


    private static Site site = Site.me()
            .setCycleRetryTimes(3)
            .setCharset("utf-8")
            .addHeader("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader())
            .addHeader("Cookie", "has_js=1; _ga=GA1.2.1625240036.1571899361; _gid=GA1.2.1550357791.1571899361; gallery_view=grid; gallery_services=; gallery_location=; gallery_status=; gallery_sort=date; gallery_featurep=0; DRUPAL_UID=-1; gallery_link=https://www.aedas.com/en/what-we-do/architecture/art-and-leisure; gallery_id=2466=/en/what-we-do/featured-projects/china-world-trade-center-phase-3c-development,2051=/en/what-we-do/featured-projects/tara-theatre,167=/en/what-we-do/architecture/art-and-leisure/st-pancras-renaissance-hotel-london,1840=/en/what-we-do/featured-projects/sandcrawler,2035=/en/what-we-do/architecture/art-and-leisure/cast,1842=/en/what-we-do/featured-projects/the-star,2033=/en/what-we-do/architecture/art-and-leisure/aylesbury-waterside-theatre,2030=/en/what-we-do/architecture/art-and-leisure/royal-and-derngate,2037=/en/what-we-do/architecture/art-and-leisure/northern-stage,2025=/en/what-we-do/architecture/art-and-leisure/prince-of-wales-theatre,2027=/en/what-we-do/architecture/art-and-leisure/london-coliseum,2043=/en/what-we-do/architecture/art-and-leisure/sadlers-wells,2049=/en/what-we-do/architecture/art-and-leisure/bridgewater-hall; gallerylink=https://www.aedas.com/en/what-we-do/architecture/art-and-leisure")
            .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 6.0.1; SM919 Build/MXB48T; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/6.2 TBS/044813 Mobile Safari/537.36 MMWEBID/5574 MicroMessenger/7.0.7.1521(0x27000735) Process/tools NetType/WIFI Language/zh_CN")
            .setTimeOut(10 * 1000);

    public WqcProcesser(CrawlerInfo crawlerInfo, PageParse... pageParses) {
        super(crawlerInfo, new WqcSearchPage(),new WqcDetilePage(),new MainPage(),new SecendPage(),new ThreePage());
    }


    @Override
    public Site getSite() {
        Random random = new Random();
        int time = random.nextInt(5000);
        return site.setSleepTime(3000 + time);
    }

}
