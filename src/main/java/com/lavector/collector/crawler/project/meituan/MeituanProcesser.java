package com.lavector.collector.crawler.project.meituan;


import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.meituan.com.page.MeiTuanCommentPage;
import com.lavector.collector.crawler.project.meituan.com.page.MeituanSearchPage;
import com.lavector.collector.crawler.project.meituan.com.page.ReportPage;
import us.codecraft.webmagic.Site;

import java.util.Random;

public class MeituanProcesser extends BaseProcessor {

    private static Site site = Site.me()
            .setCycleRetryTimes(3)
            .setCharset("utf-8")
//            .addHeader("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader())
            .addHeader("Cookie", "__mta=45513371.1567586008636.1567586473786.1567586652833.4; _lxsdk_cuid=16bfe951a90c8-044f5350adf9da-e343166-1fa400-16bfe951a90c8; ci=1; rvct=1; _hc.v=f7b09591-920f-caaa-e06e-fe23d4eaa41c.1563344366; _lxsdk=16bfe951a90c8-044f5350adf9da-e343166-1fa400-16bfe951a90c8; w_utmz=\"utm_campaign=(direct)&utm_source=(direct)&utm_medium=(none)&utm_content=(none)&utm_term=(none)\"; w_uuid=fPOi4l9xb0b7yxX8_q-AzGrWbJuwG5vxcuNfQZqNF-0r3QBUYZAMwxgCnl8hCADO; w_visitid=2e56c370-1c15-4d34-89c2-75f89c7c850a; _lx_utm=utm_source%3DBaidu%26utm_medium%3Dorganic; _ga=GA1.3.159614718.1567586008; _gid=GA1.3.243921532.1567586008; __mta=45513371.1567586008636.1567586008636.1567586008636.1; JSESSIONID=1lokjeniafuy2xrmlin8jhb1; _lxsdk_s=16cfb683901-ee1-7e1-c4c%7C%7C6")
            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36")
            .addHeader("Referer", "https://waimai.meituan.com/comment/")
            .addHeader("Accept-Language", "zh-CN,zh;q=0.9")
            .addHeader("Origin", "https://h5.waimai.meituan.com")
            .addHeader("Sec-Fetch-Mode", "cors")
            .setTimeOut(10 * 1000);


    public MeituanProcesser(CrawlerInfo crawlerInfo, PageParse... pageParses) {
        super(crawlerInfo, new MeituanSearchPage(),new MeiTuanCommentPage(),new ReportPage());
    }


    @Override
    public Site getSite() {
        Random random = new Random();
        int time = random.nextInt(10) * 1000;
        return site.setSleepTime(5000 + time);
    }

}
