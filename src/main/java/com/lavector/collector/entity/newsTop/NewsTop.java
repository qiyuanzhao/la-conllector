package com.lavector.collector.entity.newsTop;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.lavector.collector.crawler.util.JsonMapper;
import com.lavector.collector.entity.BaseAuditableSqlEntity;
import com.lavector.collector.entity.data.News;
import com.lavector.collector.entity.newsAggregation.NewsAggregationType;
import com.lavector.collector.entity.source.Origin;

import javax.persistence.*;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created on 30/10/2017.
 *
 * @author seveniu
 */
@Entity
public class NewsTop extends BaseAuditableSqlEntity {
    private String date;
    @Lob
    @JsonIgnore
    private String topList;
    private Long cid; //categoryId
    @Transient
    private List<NewsSimple> topNewsList;
    @Enumerated(EnumType.STRING)
    private NewsAggregationType type;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTopList() {
        return topList;
    }

    public void setTopList(String topList) {
        this.topList = topList;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public NewsAggregationType getType() {
        return type;
    }

    public void setType(NewsAggregationType type) {
        this.type = type;
    }

    public List<NewsSimple> getTopNewsList() {
        if (topNewsList == null) {
            try {
                this.topNewsList = JsonMapper.buildNonNullBinder().getMapper().readValue(this.topList, new TypeReference<List<NewsSimple>>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return this.topNewsList;
    }

    public void setTopNewsList(List<NewsSimple> topNewsList) {
        this.topNewsList = topNewsList;
        this.topList = JsonMapper.buildNonNullBinder().toJson(topNewsList);
    }

    public static class NewsSimple {
        private String title;
        private String author;
        private String source;
        private String url;
        private Origin origin;
        private String site;
        private Date timePublish;
        private boolean hot = true;
        private boolean manual;

        public NewsSimple() {
        }

        public NewsSimple(News news, Origin origin) {
            this.title = news.getTitle();
            this.author = news.getAuthor();
            this.source = news.getSource();
            this.url = news.getUrl();
            this.origin = origin;
            this.site = news.getSite();
            this.timePublish = news.getTimePublish();
        }

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

        public Origin getOrigin() {
            return origin;
        }

        public void setOrigin(Origin origin) {
            this.origin = origin;
        }

        public String getSite() {
            return site;
        }

        public void setSite(String site) {
            this.site = site;
        }

        public Date getTimePublish() {
            return timePublish;
        }

        public void setTimePublish(Date timePublish) {
            this.timePublish = timePublish;
        }

        public boolean isHot() {
            return hot;
        }

        public void setHot(boolean hot) {
            this.hot = hot;
        }

        public boolean isManual() {
            return manual;
        }

        public void setManual(boolean manual) {
            this.manual = manual;
        }
    }
}
