package com.lavector.collector.entity.wechatSmall.shop;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.lavector.collector.crawler.util.JsonMapper;
import com.lavector.collector.entity.BaseAuditableSqlEntity;
import com.lavector.collector.entity.wechatSmall.brand.Brand;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created on 21/12/2017.
 *
 * @author seveniu
 */
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Shop extends BaseAuditableSqlEntity {
    private String name;
    private Long plazaId;
    @ManyToOne(fetch = FetchType.EAGER)
    private Brand brand;
    private String address;
    private String phone;
    private String introduction;
    private Double rate;
    private String pics;
    @Transient
    private List<String> picList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPlazaId() {
        return plazaId;
    }

    public void setPlazaId(Long plazaId) {
        this.plazaId = plazaId;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
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

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public List<String> getPicList() {
        if (picList == null) {
            if (this.pics == null) {
                this.picList = new LinkedList<>();
            } else {
                try {
                    this.picList = JsonMapper.buildNonNullBinder().getMapper().readValue(pics, new TypeReference<List<String>>(){});
                } catch (IOException e) {
                    throw new RuntimeException();
                }
            }
        }
        return picList;
    }

    public void setPicList(List<String> picList) {
        this.picList = picList;
        this.pics = JsonMapper.buildNonNullBinder().toJson(this.picList);
    }
}
