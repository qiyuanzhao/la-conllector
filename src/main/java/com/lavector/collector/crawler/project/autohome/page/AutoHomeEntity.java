package com.lavector.collector.crawler.project.autohome.page;


import java.io.Serializable;

public class AutoHomeEntity implements Serializable{

    private String word;
    private String title;
    private String userName;
    private String date;
    private String url;
    private String content;
    private String code;
    private String view;
    private String replys;
    private String titleUrl;

    public String getWord() {
        return word;
    }

    public String getTitle() {
        return title;
    }

    public String getUserName() {
        return userName;
    }

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }

    public String getContent() {
        return content;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getView() {
        return view;
    }

    public String getReplys() {
        return replys;
    }

    public void setView(String view) {
        this.view = view;
    }

    public void setReplys(String replys) {
        this.replys = replys;
    }

    public String getTitleUrl() {
        return titleUrl;
    }

    public void setTitleUrl(String titleUrl) {
        this.titleUrl = titleUrl;
    }
}
