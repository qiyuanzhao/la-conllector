package com.lavector.collector.crawler.project.weibo.weiboStar.page;

import java.util.ArrayList;
import java.util.List;

public class Project {

    //转发
    private String report;
    //评论
    private List<String> comment = new ArrayList<>();
    //点赞
    private String like;
    //关键词
    private String keyWord;
    //内容
    private String conent;
    //详情页ID
    private String id;
    //评论数量
    private String commentNumber;


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
}

