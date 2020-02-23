package com.lavector.collector.thirdpartyapi.weixin.entity;

import com.lavector.collector.crawler.util.JsonMapper;
import com.lavector.collector.entity.wechatSmall.article.Article;

import java.util.List;

/**
 * Created on 2018/1/8.
 *
 * @author zeng.zhao
 */
public class ApiEntity {

    private Integer current_page;
    private List<ArticleEntity> articleEntities;
    private Integer from;
    private Integer last_page;
    private String next_page_url;
    private String path;
    private Integer pre_page;
    private String prev_page_url;
    private Integer to;
    private Integer total;

    public Integer getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(Integer current_page) {
        this.current_page = current_page;
    }

    public List<ArticleEntity> getArticleEntities() {
        return articleEntities;
    }

    public void setArticleEntities(List<ArticleEntity> articleEntities) {
        this.articleEntities = articleEntities;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getLast_page() {
        return last_page;
    }

    public void setLast_page(Integer last_page) {
        this.last_page = last_page;
    }

    public String getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(String next_page_url) {
        this.next_page_url = next_page_url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getPre_page() {
        return pre_page;
    }

    public void setPre_page(Integer pre_page) {
        this.pre_page = pre_page;
    }

    public String getPrev_page_url() {
        return prev_page_url;
    }

    public void setPrev_page_url(String prev_page_url) {
        this.prev_page_url = prev_page_url;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

}
