package com.lavector.collector.entity.wechatSmall.plaza;

import com.lavector.collector.entity.BaseAuditableSqlEntity;

import javax.persistence.Entity;

/**
 * Created on 22/12/2017.
 *
 * @author seveniu
 */
@Entity
public class Plaza extends BaseAuditableSqlEntity {
    private String name;
    private Double latitude;
    private Double longitude;
    private String address;
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
