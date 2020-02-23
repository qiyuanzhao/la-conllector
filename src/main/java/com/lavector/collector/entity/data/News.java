package com.lavector.collector.entity.data;

import com.lavector.collector.entity.BaseAuditableSqlEntity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created on 27/09/2017.
 *
 * @author seveniu
 */
@Table(indexes = {
        @Index(name = "url", columnList = "url", unique = true)
})
@Entity
public class News extends BaseAuditableSqlEntity {
    private String title;
    private String author;
    private String source;
    private String url;
    private String originSource;
    @Lob
    private String content;

    @Lob
    private String introduction;
    private int priority;
    private String site;
    private String category;
    private Date timePublish;
    private Long taskId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getOriginSource() {
        return originSource;
    }

    public void setOriginSource(String originSource) {
        this.originSource = originSource;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getTimePublish() {
        return timePublish;
    }

    public void setTimePublish(Date timePublish) {
        this.timePublish = timePublish;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", source='" + source + '\'' +
                ", url='" + url + '\'' +
                ", originSource='" + originSource + '\'' +
//                ", content='" + (content == null ? "" : content.substring(0, 100)) + '\'' +
                ", introduction='" + introduction + '\'' +
                ", priority=" + priority +
                ", timePublish=" + timePublish +
                ", site='" + site + '\'' +
                '}';
    }
}
