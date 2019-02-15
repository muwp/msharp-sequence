package com.ruijing.sequence.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Sequence
 *
 * @author mwup
 * @version 1.0
 * @created 2019/02/13 13:51
 **/
public class Sequence implements Serializable {

    private Long id;

    private String bizName;

    private long maxId;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBizName() {
        return bizName;
    }

    public void setBizName(String bizName) {
        this.bizName = bizName;
    }

    public long getMaxId() {
        return maxId;
    }

    public void setMaxId(long maxId) {
        this.maxId = maxId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Sequence{");
        sb.append("id=").append(id);
        sb.append(", bizName='").append(bizName).append('\'');
        sb.append(", maxId=").append(maxId);
        sb.append(", updateTime=").append(updateTime);
        sb.append('}');
        return sb.toString();
    }
}
