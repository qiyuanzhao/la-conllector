package com.lavector.collector.entity.crawlerTask;

import com.lavector.collector.entity.BaseAuditableSqlEntity;

import javax.persistence.Entity;
import javax.persistence.Lob;
import java.util.Date;

/**
 * Created on 25/09/2017.
 *
 * @author seveniu
 */
@Entity
public class CrawlerTaskLog extends BaseAuditableSqlEntity {
    private Long taskId;
    private Date timeStarted;
    private Date timeEnd;
    private long urlCount;
    private long doneCount;
    private long errorCount;
    @Lob
    private String error;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Date getTimeStarted() {
        return timeStarted;
    }

    public void setTimeStarted(Date timeStarted) {
        this.timeStarted = timeStarted;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    public long getUrlCount() {
        return urlCount;
    }

    public void setUrlCount(long urlCount) {
        this.urlCount = urlCount;
    }

    public long getDoneCount() {
        return doneCount;
    }

    public void setDoneCount(long doneCount) {
        this.doneCount = doneCount;
    }

    public long getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(long errorCount) {
        this.errorCount = errorCount;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
