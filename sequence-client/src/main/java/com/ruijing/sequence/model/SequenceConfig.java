package com.ruijing.sequence.model;

import java.io.Serializable;
import java.util.Date;

/**
 * SequenceConfig
 *
 * @author mwup
 * @version 1.0
 * @created 2019/02/13 13:51
 **/
public class SequenceConfig implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 业务名称
     */
    private String bizName;

    /**
     * id生成模式
     */
    private String mode;

    /**
     * id生成规0规则
     */
    private int type;

    /**
     * 初始化值
     */
    private long initValue;

    /**
     * 步长
     */
    private int step;

    /**
     * 客户端鉴权token
     */
    private String token;

    /**
     * 重复次数
     */
    private int retryTimes;

    /**
     * 重置时间
     */
    private Date resetTime;

    /**
     * 修改时间
     */
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

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getInitValue() {
        return initValue;
    }

    public void setInitValue(long initValue) {
        this.initValue = initValue;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getResetTime() {
        return resetTime;
    }

    public void setResetTime(Date resetTime) {
        this.resetTime = resetTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SequenceConfig{");
        sb.append("id=").append(id);
        sb.append(", bizName='").append(bizName).append('\'');
        sb.append(", mode='").append(mode).append('\'');
        sb.append(", type=").append(type);
        sb.append(", initValue=").append(initValue);
        sb.append(", step=").append(step);
        sb.append(", token='").append(token).append('\'');
        sb.append(", retryTimes=").append(retryTimes);
        sb.append(", updateTime=").append(updateTime);
        sb.append('}');
        return sb.toString();
    }
}
