package com.lavector.collector.entity.readData;

import java.io.Serializable;
import java.util.List;


public class SkuData implements Serializable{

    private String brand;
    private String url;

    private String Words;

    public String str4;

    public String str5;

    public String str6;

    private List<String> headWords;

    public Integer rowNumber = 0;

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

    public String getWords() {
        return Words;
    }

    public void setWords(String words) {
        Words = words;
    }

    public List<String> getHeadWords() {
        return headWords;
    }

    public void setHeadWords(List<String> headWords) {
        this.headWords = headWords;
    }
}
