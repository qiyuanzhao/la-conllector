package com.lavector.collector.crawler.project.dazhongdianping;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import com.lavector.collector.crawler.nonce.dianping.DianPingSearchPage;
import com.lavector.collector.crawler.nonce.dianping.DianPingShopPage;
import com.lavector.collector.crawler.project.dazhongdianping.page.DianpingSearchPage1;
import us.codecraft.webmagic.Site;

import java.util.Random;


public class DianpingPageProcessor extends BaseProcessor {

    private Site site = Site.me()
            .setRetrySleepTime(6000)
            .setCycleRetryTimes(5)
            .setTimeOut(15 * 1000)
            ;

    public DianpingPageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new DianpingSearchPage1(),new DianPingShopPage());
    }

    @Override
    public Site getSite() {
        Random random = new Random();
        int time = random.nextInt(6000);
        site.setSleepTime(1000 + time);
//        site.addHeader("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader());
        site.addHeader("Cookie", "_lxsdk_cuid=16759ecc921c8-07ac28405ab85b-6655742e-1fa400-16759ecc922c8; _lxsdk=16759ecc921c8-07ac28405ab85b-6655742e-1fa400-16759ecc922c8; _hc.v=6b8806cc-c6e5-8d09-2fe9-2c1cb715b435.1543401950; s_ViewType=10; __utma=1.2001555576.1543456299.1543456299.1543456299.1; _dp.ac.v=6d557e76-7a21-4b52-8ab2-b228993302ce; ua=dpuser_4166581570; ctu=ccdfbebe9dac58d06f758671317e16a00609b51d1bef6b549ae754858c037628; cityid=2; seouser_ab=shopList%3AA%3A1; cye=shanghai; cy=1; _lx_utm=utm_source%3Dlygl0801pc; dper=638cd635bbed0ceb80a9a0faeeeed64652ccf34d1d555a713d7869019b265a734e7ecb9baa418fd5c6a8208eb0366131374e42679fa5a7cf8818ca0b46cd15db9fcc4c399358fd0599cc0a9c4457c7a61645dcac8ddc0068c6897749fe6407a9; ll=7fd06e815b796be3df069dec7836c3df; uamo=13295431080; _lxsdk_s=16c6b5d68bc-5cf-7a8-8d3%7C%7C969");
        site.addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36");
//        site.addHeader("Referer", "http://www.dianping.com/shanghai");
        site.addHeader("host", "www.dianping.com");
        site.addHeader("Upgrade-Insecure-Requests", "1");
        return site;
    }

}
