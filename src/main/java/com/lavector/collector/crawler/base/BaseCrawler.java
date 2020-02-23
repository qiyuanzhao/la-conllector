package com.lavector.collector.crawler.base;

import com.lavector.collector.crawler.util.JsonMapper;
import com.lavector.collector.entity.crawlerTask.CrawlerTask;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.SpiderListener;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created on 25/09/2017.
 *
 */
public abstract class BaseCrawler {
    private Date startTime;
    private Date endTime;

    private MySpider spider;
    @Autowired
    BasePipeline pipeline;
    private CrawlerTask crawlerTask;


    private final Object startLock = new Object();

    public void start(CrawlerTask crawlerTask) {
        if (this.spider == null) {
            synchronized (startLock) {
                if (this.spider == null) {
                    this.crawlerTask = crawlerTask;
                    CrawlerInfo crawlerInfo = JsonMapper.buildNonNullBinder().fromJson(crawlerTask.getCrawlerInfo(), CrawlerInfo.class);
                    this.spider = this.createSpider(crawlerInfo);
                    this.spider.setCrawler(this);
                    this.startTime = new Date();
                    List<SpiderListener> spiderListeners = this.spider.getSpiderListeners();
                    if (spiderListeners == null) {
                        spiderListeners = Collections.singletonList(new DefaultSpiderListener());
                        this.spider.setSpiderListeners(spiderListeners);
                    }
                    this.spider.addPipeline(pipeline);
                    this.startTime = new Date();
                    this.spider.setCloseListener(() -> {
                        this.endTime = new Date();
                    });
                    this.spider.start();
                }
            }
        }
    }

    protected abstract MySpider createSpider(CrawlerInfo crawlerInfo);

    protected void onFinish() {

    }

    public List<CrawlerInfo> getCrawlerInfo() {
        return null;
    }

    class DefaultSpiderListener implements SpiderListener {

        @Override
        public void onSuccess(Request request) {

        }

        @Override
        public void onError(Request request) {

        }
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public CrawlerTask getCrawlerTask() {
        return crawlerTask;
    }

    public void setCrawlerTask(CrawlerTask crawlerTask) {
        this.crawlerTask = crawlerTask;
    }
}
