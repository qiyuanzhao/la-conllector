package com.lavector.collector.crawler.project.gengmei.entity;


public class Doctor {

    private String doctor_id;

    private String hospital_id;

    private String calendar_group_cnt; //案例数

    private String yuyue_count; //预约数

    private String hospital_name;

    private String url;

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

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

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
