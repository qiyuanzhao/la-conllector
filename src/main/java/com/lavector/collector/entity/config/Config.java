package com.lavector.collector.entity.config;

import com.lavector.collector.entity.BaseAuditableSqlEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created on 01/11/2017.
 *
 * @author seveniu
 */
@Entity
public class Config extends BaseAuditableSqlEntity{

    @Column(unique = true)
    private String name;
    private String configData;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfigData() {
        return configData;
    }

    public void setConfigData(String configData) {
        this.configData = configData;
    }
}
