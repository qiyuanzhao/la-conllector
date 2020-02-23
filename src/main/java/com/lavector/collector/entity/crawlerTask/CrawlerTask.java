package com.lavector.collector.entity.crawlerTask;

import com.lavector.collector.entity.BaseAuditableSqlEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import java.util.Date;

/**
 * Created on 25/09/2017.
 *
 * @author seveniu
 */
@Entity
public class CrawlerTask extends BaseAuditableSqlEntity {
    @Lob
    private String crawlerInfo;
    private String crawlerType;

    private int cycle = 0; // 周期，单位 秒， 0 表示执行一次

    private Date lastRunTime;
    private Date nextRunTime;
    @Enumerated(EnumType.STRING)
    private CrawlerTaskStatus taskStatus;
    private Long originId;


    public String getCrawlerInfo() {
        return crawlerInfo;
    }

    public void setCrawlerInfo(String crawlerInfo) {
        this.crawlerInfo = crawlerInfo;
    }

    public String getCrawlerType() {
        return crawlerType;
    }

    public void setCrawlerType(String crawlerType) {
        this.crawlerType = crawlerType;
    }

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public Date getNextRunTime() {
        return nextRunTime;
    }

    public void setNextRunTime(Date nextRunTime) {
        this.nextRunTime = nextRunTime;
    }

    public CrawlerTaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(CrawlerTaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Date getLastRunTime() {
        return lastRunTime;
    }

    public void setLastRunTime(Date lastRunTime) {
        this.lastRunTime = lastRunTime;
    }

    public Long getOriginId() {
        return originId;
    }

    public void setOriginId(Long originId) {
        this.originId = originId;
    }
}
