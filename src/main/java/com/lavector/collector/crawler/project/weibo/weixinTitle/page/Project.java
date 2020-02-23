package com.lavector.collector.crawler.project.weibo.weixinTitle.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Project implements Serializable{


    private String report;
    private List<String> comment = new ArrayList<>();
    private String like;
    private String keyWord;
    private String conent;
    private String id;
    private String commentNumber;
    private String date;
    private String name;
    private String url;
    private String title;
    private String referUrl;


    public void setReport(String report) {
        this.report = report;
    }

    public void setComment(List<String> comment) {
        this.comment = comment;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getReport() {
        return report;
    }

    public List<String> getComment() {
        return comment;
    }

    public String getLike() {
        return like;
    }


    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public void setConent(String conent) {
        this.conent = conent;
    }

    public String getConent() {
        return conent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(String commentNumber) {
        this.commentNumber = commentNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReferUrl() {
        return referUrl;
    }
    public void setReferUrl(String referUrl) {
        this.referUrl = referUrl;
    }
}

