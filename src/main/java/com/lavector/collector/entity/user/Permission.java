package com.lavector.collector.entity.user;

import com.lavector.collector.entity.BaseAuditableSqlEntity;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;

/**
 * Created on 10/10/2017.
 *
 * @author seveniu
 */
@Entity
public class Permission extends BaseAuditableSqlEntity implements GrantedAuthority {
    private String name;
    private String description;
    private String url;
    private String method;
    private Long pid; // permission id, 每两位 表示一级， 103 表示 1 -> 3


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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    @Override
    public String getAuthority() {
        return name;
    }
}
