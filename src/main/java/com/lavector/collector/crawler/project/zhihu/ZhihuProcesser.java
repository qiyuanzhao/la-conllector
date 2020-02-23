package com.lavector.collector.crawler.project.zhihu;


import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.zhihu.page.AnswerPage;
import com.lavector.collector.crawler.project.zhihu.page.QuestionPage;
import com.lavector.collector.crawler.project.zhihu.page.ZhihuSearchPage;
import us.codecraft.webmagic.Site;

import java.util.Random;

public class ZhihuProcesser extends BaseProcessor {


    public ZhihuProcesser(CrawlerInfo crawlerInfo, PageParse... pageParses) {
        super(crawlerInfo,new ZhihuSearchPage(),new QuestionPage(),new AnswerPage());
    }


    private static Site site = Site.me()
            .setCycleRetryTimes(3)
            .setCharset("utf-8")
            .addHeader("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader())
            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:65.0) Gecko/20100101 Firefox/65.0")
            .addHeader("Cookie","_zap=e795bb90-738f-43e0-b5fc-2f72a3e5d882; d_c0=\"AADihAnAlw6PTujQwq6LBGRxR5Z9UHfXKNk=|1543481948\"; _xsrf=2gTVGxTswK9QcaT6XfPKrHux68aDYh5O; __gads=ID=e8d5d7066e0ee1a3:T=1557718752:S=ALNI_Mbb5mbu0l_tD0l17dhZvkAg8_olpg; z_c0=\"2|1:0|10:1567999239|4:z_c0|92:Mi4xcU9RX0RBQUFBQUFBQU9LRUNjQ1hEaVlBQUFCZ0FsVk5CeE5qWGdBSGFtbDZRLW5TLXZ0YnJOeWhCbUZaOXYzWUNn|901cdc4d13f8bc4fbd8b16c50ca44d27d9296e0dac119e7fd88f490df1abfef3\"; tst=r; q_c1=c1901158b8304bb29664f5a2895b1543|1576122211000|1543481950000; anc_cap_id=e67861223cc9424e80bacd503fbed823; tgw_l7_route=e2ca88f7b4ad1bb6affd1b65f8997df3; Hm_lvt_98beee57fd2ef70ccdd5ca52b9740c49=1577157022,1577251606,1577252401,1577261041; Hm_lpvt_98beee57fd2ef70ccdd5ca52b9740c49=1577261512")
            .setTimeOut(10 * 1000);



    @Override
    public Site getSite() {
        Random random = new Random();
        int time = random.nextInt(10);
        return site.setSleepTime(0);
    }

}
