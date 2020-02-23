package com.lavector.collector.crawler.nonce.guiderank.entity;

import java.util.List;

/**
 * Created on 12/07/2018.
 *
 * @author zeng.zhao
 */
public class Category {

    private String categoryId;
    private String name;
    private List<Category> categoryGroups;
    private Integer type;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Category> getCategoryGroups() {
        return categoryGroups;
    }

    public void setCategoryGroups(List<Category> categoryGroups) {
        this.categoryGroups = categoryGroups;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
