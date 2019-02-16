package com.ruijing.sequence.builder;

import com.ruijing.sequence.sequence.snowflake.SnowflakeSequence;
import com.ruijing.sequence.service.Sequence;

/**
 * 基于雪花算法，序列号生成器构建者
 *
 * @author mwup
 * @version 1.0
 * @created 2019/02/13 13:51
 **/
public class SnowflakeSeqBuilder implements SeqBuilder {

    /**
     * 数据中心ID，值的范围在[0,31]之间，一般可以设置机房的IDC[必选]
     */
    private long datacenterId;

    /**
     * 工作机器ID，值的范围在[0,31]之间，一般可以设置机器编号[必选]
     */
    private long workerId;

    @Override
    public Sequence build() {
        SnowflakeSequence sequence = new SnowflakeSequence();
        sequence.setDataCenterId(this.datacenterId);
        sequence.setWorkerId(this.workerId);
        return sequence;
    }

    public static SnowflakeSeqBuilder custom() {
        SnowflakeSeqBuilder builder = new SnowflakeSeqBuilder();
        return builder;
    }

    public SnowflakeSeqBuilder datacenterId(long datacenterId) {
        this.datacenterId = datacenterId;
        return this;
    }

    public SnowflakeSeqBuilder workerId(long workerId) {
        this.workerId = workerId;
        return this;
    }

}
