package com.lavector.collector.crawler.project.yanyuewang;

import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.yanyuewang.page.YanyuewangSearchPage;
import us.codecraft.webmagic.Site;

import java.util.Random;

/**
 * Created by qyz on 2019/8/26.
 */
public class YanyuewangProcesser extends BaseProcessor {

    public YanyuewangProcesser(CrawlerInfo crawlerInfo, PageParse... pageParses) {
        super(crawlerInfo,new YanyuewangSearchPage());
    }


    private static Site site = Site.me()
            .setCycleRetryTimes(3)
            .setCharset("utf-8")
            .addHeader("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader())
            .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:65.0) Gecko/20100101 Firefox/65.0")
            .addHeader("cookie","tgw_l7_route=a37704a413efa26cf3f23813004f1a3b; _zap=4109b03b-d094-4e9f-a432-e3707d926f2d; _xsrf=1D6UFDvc2FCiWEnqlj8pkobM5Hd52rFX; d_c0=\"AJDjnuxX6g-PTqUQUKVC3ai-dYhMh6TmfGI=|1566204560\"; capsion_ticket=\"2|1:0|10:1566204560|14:capsion_ticket|44:ZGNiM2VkZGRiZTQ1NDlkMGFjOWI3ZDk4MGNmNGI4NmI=|e78fa2cbffb986fc3c9c0e17f33fa9791f17160cab4363f3fcdf5a88278f1fe8\"; z_c0=\"2|1:0|10:1566204578|4:z_c0|92:Mi4xcU9RX0RBQUFBQUFBa09PZTdGZnFEeVlBQUFCZ0FsVk5vckJIWGdENkxqNlN0QVFfQkxaMGpFSGlHby1ITG40bjVR|5dde9cb8d32b65e13dc2b4d0d82bf1f7ce7de26f4e5f84fde0bf0a7781e683a9\"; unlock_ticket=\"AHAm2KrfOg4mAAAAYAJVTappWl00yy2eiQjksYnh9JNdIKK0my_yQA==\"; tst=r")
            .setTimeOut(10 * 1000);


    @Override
    public Site getSite() {
        Random random = new Random();
        int time = random.nextInt(10);
        return site.setSleepTime(0);
    }


}
