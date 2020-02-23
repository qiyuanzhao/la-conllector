package com.lavector.collector.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import java.util.Date;


@MappedSuperclass
public class BaseAuditableSqlEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Enumerated(EnumType.STRING)
    protected EntityStatus status = EntityStatus.ACTIVE;
    /**
     * bitmask flags
     */
    protected Integer flags;

    protected Date timeCreated;
    protected Date timeUpdated;
    protected Long createdBy;
    protected Long updatedBy;

    protected BaseAuditableSqlEntity() {
    }

    @JsonIgnore
    public EntityStatus getStatus() {
        return status;
    }

    public void setStatus(EntityStatus status) {
        this.status = status;
    }

    @JsonIgnore
    public boolean isActive() {
        return this.status == EntityStatus.ACTIVE;
    }

    public Integer getFlags() {
        return flags;
    }

    public void setFlags(Integer flags) {
        this.flags = flags;
    }

    public boolean getFlag(int flag) {
        if (this.flags == null) {
            this.flags = 0;
        }
        boolean flagOn = (this.flags & flag) > 0;
        return flagOn;
    }

    public void setFlag(int flag) {
        if (this.flags == null) {
            this.flags = 0;
        }
        this.flags = this.flags | flag;
    }

    public void resetFlag(int flag) {
        if (this.flags == null) {
            this.flags = 0;
        }
        this.flags = this.flags & ~flag;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public Date getTimeUpdated() {
        return timeUpdated;
    }

    public void setTimeUpdated(Date timeUpdated) {
        this.timeUpdated = timeUpdated;
    }

}
