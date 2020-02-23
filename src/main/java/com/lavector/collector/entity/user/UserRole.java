package com.lavector.collector.entity.user;


import com.lavector.collector.entity.BaseAuditableSqlEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Set;

/**
 * Created on 10/10/2017.
 *
 * @author seveniu
 */
@Entity
public class UserRole extends BaseAuditableSqlEntity {
    private String name;

    @ManyToMany
    private Set<Permission> permissions;

    private Long projectId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }


    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
