package com.lavector.collector.crawler.project.weixinmall;


import com.lavector.collector.crawler.base.BaseProcessor;
import com.lavector.collector.crawler.base.CrawlerInfo;
import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.weixinmall.page.WeixinMallSearchPage;
import com.lavector.collector.crawler.project.wqc.page.MainPage;
import com.lavector.collector.crawler.project.wqc.page.SecendPage;
import com.lavector.collector.crawler.project.wqc.page.ThreePage;
import com.lavector.collector.crawler.project.wqc.page.WqcDetilePage;
import com.lavector.collector.crawler.project.wqc.page.WqcSearchPage;
import us.codecraft.webmagic.Site;

import java.util.Random;

public class WeixinMallProcesser extends BaseProcessor {


    private static Site site = Site.me()
            .setCycleRetryTimes(3)
            .setCharset("utf-8")
            .addHeader("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader())
            .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjVhYjI3MjU3MjJjMDhiZjYxYzJkM2YzN2E3MjE1OWE0Y2I0ZTE0Yzc0OTM1ZTk4ZTgxNGFkNzQ2YzdjMDU4OGFkNTI5MmVlNDJjMTFmM2MwIn0.eyJhdWQiOiIyIiwianRpIjoiNWFiMjcyNTcyMmMwOGJmNjFjMmQzZjM3YTcyMTU5YTRjYjRlMTRjNzQ5MzVlOThlODE0YWQ3NDZjN2MwNTg4YWQ1MjkyZWU0MmMxMWYzYzAiLCJpYXQiOjE1NzQyMTY4MjAsIm5iZiI6MTU3NDIxNjgyMCwiZXhwIjoxNTc0MjI0MDIwLCJzdWIiOiIyIiwic2NvcGVzIjpbImJhc2ljIl19.rS563Z1l1FvHxb0EH0zo1cQnZ4YqmpTEPytcuaZAUV8i0-pcdW3v5IVb5ipQub7ke03Xh7eestYAqZrR4bjKXbGCsILTFt84Y6gkaXyuhkUMBlRfl8T7UoMtuYPLR6N-R5sEXXqEKoyywpos1_hdUcMBOkTPWHhpD6kPfHaGVxZLGWhsPqjD5HJPkvZRZOtsKbLPx_eWuE_usNdimFVIABZ-dw5UrPL39HK07NN9Dfajf0GrCKKAkX1Tl0f5AR3PaIcII8XsGS5P6XjIqFD1h6cbzbFm1ej-bvSEAe__Xd9DkBMevSm9OTa7caAsy2AeKPuOi-EmHiOxHt6RgDUKT8Drg3thO47zupjsMIrjp94H3XaocJuJQj1HnPW00UClbMQJCQ5YQKbJ9W2GlMnKNjxXRTa3-XDVacrI1pqgR725j4GSmCwJ7MKhr4jwRIZLlBncESvJaKsfvzgJf6B1lInih9KNQXa7pwBxJujc75pSXbWoDl5Rcti96POAgu2QOxGJEsHqVCgLLYgauJg21ApAPv_2Dohjc1bdBaIKlcI-EfHyCbjEhhEh0MsDaNGw_1OezebqjHkPZyD-HK5j5ew-_C4WR5b8Ks-66-ViXreyfz5pmJYVrRkI5yACkcPyF1v1gMADvRYElRZYCF6r4GJhEt6hTPcOZ5rxids5mZM")
            .setTimeOut(10 * 1000);

    public WeixinMallProcesser(CrawlerInfo crawlerInfo, PageParse... pageParses) {
        super(crawlerInfo, new WeixinMallSearchPage());
    }


    @Override
    public Site getSite() {
        Random random = new Random();
        int time = random.nextInt(5000);
        return site.setSleepTime(3000/* + time*/);
    }

}
