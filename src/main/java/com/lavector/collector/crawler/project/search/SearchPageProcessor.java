package com.lavector.collector.crawler.project.search;

import com.google.common.collect.Sets;
import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.project.search.page.SearchListPage;
import com.lavector.collector.crawler.project.search.page.ThreadListPage;
import us.codecraft.webmagic.Site;

/**
 * Created on 2017/11/30.
 *
 * @author zeng.zhao
 */
public class SearchPageProcessor extends BaseProcessor {

    private final Site site = Site.me()
            .setAcceptStatCode(Sets.newHashSet(200, 302, 307))
            .setTimeOut(30 * 1000)
            .setCycleRetryTimes(3)
            .setRetrySleepTime(2000)
            .setCharset("gbk")
            .setSleepTime(2000)
            .addHeader("cache-control", "max-age=0")
            .addHeader("cookie", "CYHTooltip=1; listvideopopup=1; fvlid=1511148456623h6ReOHU6k5; sessionid=9B3AE573-755F-4A36-AC94-86D5B7126FF8%7C%7C2017-11-20+11%3A27%3A36.945%7C%7C0; ahpau=1; sessionfid=3629974514; pcpopclub=F54DF6CA872E835C001C89F64821E5638BE2B0C58FD47222222630E1129D6B084D3D944022BADFDC9DC3DDD46A8E0178C60DBA7E416661895A446820CCAB9B2B1764F661C6B71F2747127B6A668507A0EBF018B060ED65E7080478F7F5D03CDFC3E4997E2E88457AAB3B70BDC2C0AC656F683AFBC678A7256BA0319D79E81C46055830A01AE4A536C84168B01D66849A7872BB7F2984764FF08725D02FFF1F7A80FE3EF05AC0C05EBD9AEDA5D3A0499862EC59A276463355A356C4C03BD933D305D754DB9924104EBAE15BEF3015B59294DD27953AC719D86F14881BA6215795DFACC04291767DC743B5B844E6805D34A57A8FF6DAA157023B0D59DC9F005D771FB4144E99039B3F5B75465DFBDD35854341E3A13D6115D05E2EA1C923C3E2779F8ACAB205A936839214522C2AF68D9C924BCA00; clubUserShow=63208696|4305|2|%e5%a4%aa%e7%a9%ba%e7%9a%84%e6%b0%a7%e6%b0%94|0|0|0||2017-12-01 18:28:14|0; autouserid=63208696; sessionuserid=63208696; sessionlogin=d9b5c78cca404a988bd68ef5f1372bd803c47cf8; __utma=1.375448252.1512124039.1512124039.1512124039.1; __utmc=1; __utmz=1.1512124039.1.1.utmcsr=club.autohome.com.cn|utmccn=(referral)|utmcmd=referral|utmcct=/bbs/threadqa-c-538-68906864-1.html; sessionuid=9B3AE573-755F-4A36-AC94-86D5B7126FF8%7C%7C2017-11-20+11%3A27%3A36.945%7C%7C0; sessionip=221.218.29.82; papopclub=AE401BC916510B3FDFB130E8CC71594A; pepopclub=0E9F99E7AF6573A9C1DA36703FCAEE1A; historybbsName4=c-505%7C%E9%9B%85%E5%8A%9B%E5%A3%AB%2Cc-4480%7C%E5%A5%94%E5%A5%94mini-e%2Cc-538%7C%E5%A5%A5%E8%BF%AAA5%2Cc-4352%7CALPINA%20B3%2Cc-3554%7C%E6%98%82%E7%A7%91%E5%A8%81%2Cc-2098%7CAC%20Schnitzer; ahpvno=173; CNZZDATA1262640694=72599395-1512011116-https%253A%252F%252Fwww.autohome.com.cn%252F%7C1512128926; Hm_lvt_9924a05a5a75caf05dbbfb51af638b07=1512012012,1512029812,1512093915; Hm_lpvt_9924a05a5a75caf05dbbfb51af638b07=1512130741; ref=www.baidu.com%7C0%7C0%7C0%7C2017-12-01+20%3A19%3A04.641%7C2017-11-30+11%3A20%3A11.515; sessionvid=979EFC1A-255F-4161-A9AB-B752597F368A; area=110199; cn_1262640694_dplus=%7B%22distinct_id%22%3A%20%2215fd7778d321a-0960bb8a4f8996-31627c01-13c680-15fd7778d337f0%22%2C%22sp%22%3A%20%7B%22%24_sessionid%22%3A%200%2C%22%24_sessionTime%22%3A%201512130768%2C%22%24dp%22%3A%200%2C%22%24_sessionPVTime%22%3A%201512130768%7D%7D; UM_distinctid=15fd7778d321a-0960bb8a4f8996-31627c01-13c680-15fd7778d337f0")
            .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36")
            ;

    public SearchPageProcessor(CrawlerInfo crawlerInfo) {
        super(crawlerInfo, new SearchListPage(), new ThreadListPage());
    }

    @Override
    public Site getSite() {
        return site;
    }
}
