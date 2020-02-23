package com.lavector.collector.crawler.nonce.dianping.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

/**
 * Created on 14/05/2018.
 *
 * @author zeng.zhao
 */
public class Person {

    private String userId;

    private String username;

    public Person(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public Person() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
