package com.lavector.collector.crawler.project.babyTreeUserInfo.page;


import java.io.Serializable;

public class TaoBaoPo implements Serializable{

    private String detailUrl; //产品链接
    private String title;   //标题
    private String viewSales; //付款人数
    private String commentCount; //评论数
    private String viewPrice; //价格
    public String nick;//店名

    private String userName; //用户名
    private String commentTime; //评论时间
    private String score;   //评分
    private String comment; //评论
    public String type; //类型     天猫，天猫国际，全球购....


    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setViewSales(String viewSales) {
        this.viewSales = viewSales;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public void setViewPrice(String viewPrice) {
        this.viewPrice = viewPrice;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getViewSales() {
        return viewSales;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public String getViewPrice() {
        return viewPrice;
    }

    public String getUserName() {
        return userName;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public String getScore() {
        return score;
    }

    public String getComment() {
        return comment;
    }
}
