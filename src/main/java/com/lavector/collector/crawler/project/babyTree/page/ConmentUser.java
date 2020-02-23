package com.lavector.collector.crawler.project.babyTree.page;


import java.io.Serializable;

public class ConmentUser implements Serializable{

    private String name;
    private String age;
    private String address;
    private String date;
    private String content;

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
