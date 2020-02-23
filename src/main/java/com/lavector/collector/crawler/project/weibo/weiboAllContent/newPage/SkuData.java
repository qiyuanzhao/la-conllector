package com.lavector.collector.crawler.project.weibo.weiboAllContent.newPage;

import java.util.List;


public class SkuData {

    private String brand;
    private String url;

    private List<String> Words;

    private List<String> headWords;


    public String getBrand() {
        return brand;
    }


    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getWords() {
        return Words;
    }

    public void setWords(List<String> words) {
        Words = words;
    }

    public List<String> getHeadWords() {
        return headWords;
    }

    public void setHeadWords(List<String> headWords) {
        this.headWords = headWords;
    }
}
