package com.lavector.collector.crawler.project.gengmei.entity;


public class Person {

    private String sex;

    private String age;

    private String region;

    private String oxygenContent; //养分

    private String empiricalValue;//经验值

    private String url;


    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getOxygenContent() {
        return oxygenContent;
    }

    public void setOxygenContent(String oxygenContent) {
        this.oxygenContent = oxygenContent;
    }

    public String getEmpiricalValue() {
        return empiricalValue;
    }

    public void setEmpiricalValue(String empiricalValue) {
        this.empiricalValue = empiricalValue;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
