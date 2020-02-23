package com.lavector.collector.entity.source;

import com.lavector.collector.entity.BaseAuditableSqlEntity;

import javax.persistence.Entity;

/**
 * Created on 18/10/2017.
 *
 * @author seveniu
 */
@Entity
public class Origin extends BaseAuditableSqlEntity {
    private Long categoryId;
    private String name;
    private String url;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
