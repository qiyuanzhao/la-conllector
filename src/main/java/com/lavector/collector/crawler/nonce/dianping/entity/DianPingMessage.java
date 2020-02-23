package com.lavector.collector.crawler.nonce.dianping.entity;

import org.hibernate.annotations.GeneratorType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;
import java.util.Map;

/**
 * Created on 14/05/2018.
 *
 * @author zeng.zhao
 */
public class DianPingMessage {

    private String shopId;

    private String content;

    private Person person;

    private String rank;

    private String time;

    private String shopName;

    private List<Map<String, String>> reviewRank;

    private MessageType type;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Map<String, String>> getReviewRank() {
        return reviewRank;
    }

    public void setReviewRank(List<Map<String, String>> reviewRank) {
        this.reviewRank = reviewRank;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DianPingMessage) {
            DianPingMessage o = (DianPingMessage) obj;
            return o.getShopId().equals(this.shopId) &&
                    o.getShopName().equals(this.shopName) &&
                    o.getType().equals(this.getType());
        }
        return false;
    }
}
