package com.lavector.collector.crawler.project.autohome.crawler;

import com.google.common.collect.Sets;
import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.autohome.page.LuntanSearchPage;
import us.codecraft.webmagic.Site;


public class AutoHomeProcessor extends BaseProcessor {

    private final Site site = Site.me()
            .setAcceptStatCode(Sets.newHashSet(200, 302, 307))
            .setTimeOut(30 * 1000)
            .setCycleRetryTimes(3)
            .setRetrySleepTime(2000)
            .setCharset("gbk")
            .setSleepTime(5000)
//            .addHeader("cache-control", "max-age=0")
//            .addHeader("Connection","keep-alive")
            .addHeader("cookie", "sessionip=43.254.91.82; sessionid=4F9CAAA1-3084-496B-9255-EEA0DF96B2D7%7C%7C2018-11-30+15%3A16%3A45.562%7C%7C0; area=110105; __ah_uuid=32167255-89A5-4D39-84DD-1086C31A5E57; fvlid=1543562201097IL4wrxg3ty; sessionuid=4F9CAAA1-3084-496B-9255-EEA0DF96B2D7%7C%7C2018-11-30+15%3A16%3A45.562%7C%7C0; ahpau=1; Hm_lvt_9924a05a5a75caf05dbbfb51af638b07=1548997477; ahsids=2357_4078_2664; historyClub=4428%2C4708%2C4420%2C4080; __utma=1.1877676951.1550469783.1550469783.1550469783.1; __utmc=1; __utmz=1.1550469783.1.1.utmcsr=autohome.com.cn|utmccn=(referral)|utmcmd=referral|utmcct=/beijing/; pcpopclub=dfd072e694ce4f81bf596f818a29b50005a80b53; clubUserShow=94899027|66|2|aq8m3cies|0|0|0||2019-02-18 14:03:21|2; autouserid=94899027; sessionuserid=94899027; sessionlogin=53072386f03c47c4a28bc8322dcfebd405a80b53; sessionvid=12AA8994-84DA-442F-A656-9A87E498E9F0; historybbsName4=c-4080%7C%E8%8D%A3%E5%A8%81RX5%2FRX5%E6%96%B0%E8%83%BD%E6%BA%90%2Cc-4309%7C%E4%B8%AD%E5%8D%8EH230EV%2Cc-4845%7C%E7%BA%A2%E6%97%97H7%E6%96%B0%E8%83%BD%E6%BA%90%2Cc-4420%7C%E4%B8%9C%E9%A3%8E%E9%A3%8E%E7%A5%9EAX7%E6%96%B0%E8%83%BD%E6%BA%90%2Cc-4708%7CKONA%E6%96%B0%E8%83%BD%E6%BA%90%2Cc-2761%7C%E7%A7%A6%2Cc-4419%7C%E6%B1%89%E8%85%BEX5%E6%96%B0%E8%83%BD%E6%BA%90; pvidchain=101061,101061,101061,101061,101061,101061,3311668,3311668,3311668,2108152; papopclub=DE5C32F92EBDA700B355C439E62268EA; pbcpopclub=d3b05256-1227-4404-a9e3-cbd47f84f3a0; pepopclub=0A85A9F54FD6ACC59D0175828975093D; ahpvno=71; ref=www.baidu.com%7C0%7C0%7Cwww.sogou.com%7C2019-02-18+18%3A12%3A52.061%7C2019-02-01+10%3A55%3A01.164")
            .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.80 Safari/537.36");
//            .addHeader("Host","msi.autohome.com.cn");

    public AutoHomeProcessor(CrawlerInfo crawlerInfo, PageParse... pageParses) {
        super(crawlerInfo, pageParses);
    }


    @Override
    public Site getSite() {
        return site;
    }

}
