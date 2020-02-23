package com.lavector.collector.entity.wechatSmall.favorite;

import com.lavector.collector.entity.BaseAuditableSqlEntity;
import com.lavector.collector.entity.wechatSmall.shop.Shop;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created on 21/12/2017.
 *
 * @author seveniu
 */
@Entity
public class Favorite extends BaseAuditableSqlEntity {

    private String uid;
    @ManyToOne
    private Shop shop;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
}
