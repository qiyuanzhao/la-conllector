package com.lavector.collector.crawler.project.gengmei.entity;

/**
 * 安心购
 */
public class ArrProduct {

    private String pid;  //产品id

    private String title; //名称

    private String price_online; //价格

    private String hospital_name; //医院

    private String doctor_name; //医生

    private String order_auto_cnt; //预约数

    private String calendar_cnt; //日记数

    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice_online() {
        return price_online;
    }

    public void setPrice_online(String price_online) {
        this.price_online = price_online;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getOrder_auto_cnt() {
        return order_auto_cnt;
    }

    public void setOrder_auto_cnt(String order_auto_cnt) {
        this.order_auto_cnt = order_auto_cnt;
    }

    public String getCalendar_cnt() {
        return calendar_cnt;
    }

    public void setCalendar_cnt(String calendar_cnt) {
        this.calendar_cnt = calendar_cnt;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ArrProduct{" +
                "title='" + title + '\'' +
                ", price_online='" + price_online + '\'' +
                ", hospital_name='" + hospital_name + '\'' +
                ", doctor_name='" + doctor_name + '\'' +
                ", order_auto_cnt='" + order_auto_cnt + '\'' +
                ", calendar_cnt='" + calendar_cnt + '\'' +
                ", pid='" + pid + '\'' +
                '}';
    }
}
