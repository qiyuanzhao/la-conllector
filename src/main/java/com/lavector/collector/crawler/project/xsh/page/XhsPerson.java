package com.lavector.collector.crawler.project.xsh.page;


public class XhsPerson {

    private String userName;
    private String brief;
    private String local;
    private String note;
    private String fans;
    private String collect;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getFans() {
        return fans;
    }

    public void setFans(String fans) {
        this.fans = fans;
    }

    public String getCollect() {
        return collect;
    }

    public void setCollect(String collect) {
        this.collect = collect;
    }

    @Override
    public String toString() {
        return "XhsPerson{" +
                "userName='" + userName + '\'' +
                ", brief='" + brief + '\'' +
                ", local='" + local + '\'' +
                ", note='" + note + '\'' +
                ", fans='" + fans + '\'' +
                ", collect='" + collect + '\'' +
                '}';
    }
}
