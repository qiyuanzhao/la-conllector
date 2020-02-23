package com.lavector.collector.crawler.project.gengmei.entity;


import java.util.List;

public class Hospital {

    private String hospital_id;

    private String calendar_group_cnt; //案例数

    private String yuyue_count; //预约数

    private List<ArrProduct> products;

    private String address; //地址

    private String name_cn; //名称

    private String url;

    public String getHospital_id() {
        return hospital_id;
    }

    public void setHospital_id(String hospital_id) {
        this.hospital_id = hospital_id;
    }

    public String getCalendar_group_cnt() {
        return calendar_group_cnt;
    }

    public void setCalendar_group_cnt(String calendar_group_cnt) {
        this.calendar_group_cnt = calendar_group_cnt;
    }

    public String getYuyue_count() {
        return yuyue_count;
    }

    public void setYuyue_count(String yuyue_count) {
        this.yuyue_count = yuyue_count;
    }

    public List<ArrProduct> getProducts() {
        return products;
    }

    public void setProducts(List<ArrProduct> products) {
        this.products = products;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName_cn() {
        return name_cn;
    }

    public void setName_cn(String name_cn) {
        this.name_cn = name_cn;
    }
}

