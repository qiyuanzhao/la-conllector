package com.lavector.collector.entity.user;


import com.lavector.collector.entity.BaseAuditableSqlEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @author tao
 */
@Entity
public class ResetPasswordToken extends BaseAuditableSqlEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    private User user;

    @Column(unique = true)
    private String token;

    public ResetPasswordToken() {
    }

    public ResetPasswordToken(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
