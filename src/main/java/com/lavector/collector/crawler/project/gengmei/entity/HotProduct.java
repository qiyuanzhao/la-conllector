package com.lavector.collector.crawler.project.gengmei.entity;


public class HotProduct {

    private String hospital_id;

    private String doctor_id;

    private String hospital_name; //医院名称

    private String pid; //产品id

    private String doctor_name;

    private String price; //价格

    private String comment_cnt; //评论

    private String view_cnt; //浏览量

    private String favor_cnt; //点赞

    private String item_name; //项目


    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getHospital_id() {
        return hospital_id;
    }

    public void setHospital_id(String hospital_id) {
        this.hospital_id = hospital_id;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getComment_cnt() {
        return comment_cnt;
    }

    public void setComment_cnt(String comment_cnt) {
        this.comment_cnt = comment_cnt;
    }

    public String getView_cnt() {
        return view_cnt;
    }

    public void setView_cnt(String view_cnt) {
        this.view_cnt = view_cnt;
    }

    public String getFavor_cnt() {
        return favor_cnt;
    }

    public void setFavor_cnt(String favor_cnt) {
        this.favor_cnt = favor_cnt;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }
}
