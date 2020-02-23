package com.lavector.collector.crawler.project.tmall.page.shop;

import com.lavector.collector.crawler.base.DynamicProxyDownloader;
import com.lavector.collector.crawler.project.tmall.page.TmallPageParse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created on 2018/11/23.
 *
 * @author zeng.zhao
 */
public class TmallShopPageProcessor implements PageProcessor {

    private Logger logger = LoggerFactory.getLogger(TmallShopPageProcessor.class);

    private TmallPageParse[] tmallPageParses;

    public TmallShopPageProcessor(TmallPageParse... tmallPageParses) {
        this.tmallPageParses = tmallPageParses;
    }

    @Override
    public void process(Page page) {
        for (TmallPageParse tmallPageParse : tmallPageParses) {
            if (tmallPageParse.handlerRequest(page.getRequest())) {
                tmallPageParse.parse(page);
                return;
            }
        }
        logger.info("can't handle url. {}", page.getUrl());
    }

    private Site site = Site.me()
            .setCycleRetryTimes(3)
            .setSleepTime(4000)
            .setTimeOut(10 * 1000)
            .setCharset("gbk")
            .addHeader("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader())
            .addHeader("cookie", "UM_distinctid=165a8a4788798-04d0ebfd44885b-34607908-13c680-165a8a478884ff; cna=pGeVE+6G2m0CASv+W1KGw7+T; pnm_cku822=; enc=Cxz3t%2FrXk5MtVWtl6rk0eHKJLMefL4i0TRlWPIMZJ2jjpemlXFL1jhK9sUf%2F6Oa%2FXsajPUpx3bUymfGNbhbWIQ%3D%3D; cq=ccp%3D1; x5sec=7b2273686f7073797374656d3b32223a226633323037383139653037633666633962356135623636323739626261343630434f2b363774384645502f38774d57642b35694b66673d3d227d; isg=BKamDEdlwcpxTJUaRvzVyqeU9xroL9iexFf11pBPkkmlE0Yt-Bc6UYzlb086u-JZ")
            .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");

    @Override
    public Site getSite() {
        return site;
    }
}
