package com.lavector.collector.entity.category;

import com.lavector.collector.entity.BaseAuditableSqlEntity;

import javax.persistence.Entity;

/**
 * Created on 18/10/2017.
 *
 * @author seveniu
 */
@Entity
public class Category extends BaseAuditableSqlEntity {
    private String name;
    private String code;
    private String description;
    private Long pid; // parent id, 0 表示 root

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
