package com.lavector.collector.crawler.project.weibo.weiboPepsiCola.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Project implements Serializable{

    //转发
    private String report;
    //评论
    private List<String> comment = new ArrayList<>();
    //点赞
    private String like;
    //关键词
    private String keyWord;
    //
    private String conent;
    private String id;
    //评论数量
    private String commentNumber;
    //日期
    private String date;
    //用户名称
    private String userName;

    private String url;

    private String type;

    private String name;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String name) {
        this.userName = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

