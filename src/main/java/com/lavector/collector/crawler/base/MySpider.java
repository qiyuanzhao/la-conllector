package com.lavector.collector.crawler.base;

import com.lavector.collector.crawler.project.food.NewDianpingDownloader;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

/**
 * Created on 28/09/2017.
 *
 * @author seveniu
 */
public class MySpider extends Spider {
    private BaseCrawler crawler;
    private CloseListener closeListener;
    private CrawlerInfo crawlerInfo;

    public MySpider(BaseProcessor pageProcessor) {
        super(pageProcessor);
        this.crawlerInfo = pageProcessor.getCrawlerInfo();
    }

    public static MySpider create(BaseProcessor pageProcessor) {
        return new MySpider(pageProcessor);
    }

    public static MySpider createUseProxy(BaseProcessor pageProcessor) {
        MySpider spider = create(pageProcessor);
        HttpClientDownloader downloader = new NewDianpingDownloader();
        downloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("s2.proxy.mayidaili.com", 8123)));
        spider.setDownloader(downloader);
        return spider;
    }
    @Override
    public void run() {
        super.run();
        if (closeListener != null) {
            closeListener.close();
        }

        if (threadPool.getThreadAlive() == 0 && exitWhenComplete) {
            onFinish();
        }
    }

    private void onFinish() {
        logger.info("onFinish: crawler=" + getCrawler());
        if (getCrawler() != null) {
            this.crawler.onFinish();
        }
    }

    public CloseListener getCloseListener() {
        return closeListener;
    }

    public void setCloseListener(CloseListener closeListener) {
        this.closeListener = closeListener;
    }

    public BaseCrawler getCrawler() {
        return crawler;
    }

    public void setCrawler(BaseCrawler crawler) {
        this.crawler = crawler;
    }

    public interface CloseListener {
        void close();
    }


    public CrawlerInfo getCrawlerInfo() {
        return crawlerInfo;
    }

    public void setCrawlerInfo(CrawlerInfo crawlerInfo) {
        this.crawlerInfo = crawlerInfo;
    }
}
