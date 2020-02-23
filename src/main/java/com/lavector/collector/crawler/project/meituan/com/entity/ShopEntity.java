package com.lavector.collector.crawler.project.meituan.com.entity;


public class ShopEntity {

    //店铺id
    private String id;

    //店名
    private String title;

    //地理信息
    private String address;

    //个人平均消费
    private String avgprice;

    //最低价
    private String lowestprice;

    //评分
    private String avgscore;

    //评论数
    private String comments;

    //后链名称
    private String backCateName;

    //区域名称
    private String areaname;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvgprice() {
        return avgprice;
    }

    public void setAvgprice(String avgprice) {
        this.avgprice = avgprice;
    }

    public String getLowestprice() {
        return lowestprice;
    }

    public void setLowestprice(String lowestprice) {
        this.lowestprice = lowestprice;
    }

    public String getAvgscore() {
        return avgscore;
    }

    public void setAvgscore(String avgscore) {
        this.avgscore = avgscore;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getBackCateName() {
        return backCateName;
    }

    public void setBackCateName(String backCateName) {
        this.backCateName = backCateName;
    }

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }
}
