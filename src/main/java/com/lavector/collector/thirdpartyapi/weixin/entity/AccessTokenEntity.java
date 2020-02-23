package com.lavector.collector.thirdpartyapi.weixin.entity;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * Created on 2018/1/4.
 *
 * @author zeng.zhao
 */
public class AccessTokenEntity implements Serializable {


    private static final long serialVersionUID = -3680041384621400202L;

    private String access_token;

    private String refresh_token;

    private Long expires;

    private Long start;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        if (StringUtils.isBlank(access_token)) {
            throw new RuntimeException("access_token can not be null!");
        }
        this.access_token = access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        if (StringUtils.isBlank(refresh_token)) {
            throw new RuntimeException("refresh_token can not be null!");
        }
        this.refresh_token = refresh_token;
    }

    public Long getExpires() {
        return expires;
    }

    public void setExpires(Long expires) {
        this.expires = expires;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }
}
