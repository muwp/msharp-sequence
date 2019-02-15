package com.ruijing.sequence.sequence.snowflake;

import com.ruijing.sequence.enums.ModeEnum;
import com.ruijing.sequence.service.Sequence;

import java.util.Random;

/**
 * 唯一id生成器(全局id生成器)
 *
 * @author mwup
 * @version 1.0
 * @created 2017/4/21 00:13
 **/
public class SnowflakeSequence implements Sequence {

    /**
     * 考虑到数据库在数量大的条件下分表的情况，所以id最后的序列号是以0-9之间的随机数开始的
     */
    private static final Random RANDOM = new Random();

    /**
     * 业务结点序列号
     */
    private long workerId;

    /**
     * 数据中心(或机器节点)序列号
     */
    private long dataCenterId;

    /**
     * (2018-01-01) 这个时间设置后不能再修改,防止id重叠
     */
    private final long epoch = 1514736000000L;

    /**
     * 业务节点ID长度(以二进制的形式)
     */
    private final long workerIdBits = 6L;

    /**
     * 数据中心ID长度(以二进制的形式)
     */
    private final long dataCenterIdBits = 4L;

    /**
     * 序列号12位(以二进制的形式)
     */
    private final long sequenceBits = 12L;

    /**
     * 最大支持业务节点数0~63，一共64个
     */
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

    /**
     * 最大支持数据中心节点数0~15，一共16个
     */
    private final long maxDataCenterId = -1L ^ (-1L << dataCenterIdBits);

    /**
     * 业务节点左移12位
     */
    private final long workerIdShift = sequenceBits;

    /**
     * 数据中心节点左移18位
     */
    private final long dataCenterIdShift = sequenceBits + workerIdBits;

    /**
     * 时间毫秒数左移22位
     */
    private final long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;

    /**
     * ID生成的序列号最大值
     */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    /**
     * ID生成的序列号
     */
    private long sequence = 0L;

    /**
     * 时间戳
     */
    private long lastTimestamp = -1L;

    public SnowflakeSequence() {
        this(0L, 0L);
    }

    /**
     * 初始化数据中心节序数,业务节点序数
     *
     * @param workerId     数据中心节序数
     * @param dataCenterId 业务节点序数
     */
    public SnowflakeSequence(final long dataCenterId, final long workerId) {
        this.validCheck(dataCenterId, workerId);
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }

    /**
     * 同步返回全局id
     */
    @Override
    public final synchronized Long nextId(String bizName) {
        long timestamp = currentTimeMillis();
        if (timestamp < lastTimestamp) {
            final long offset = lastTimestamp - timestamp;
            if (offset <= 5) {
                try {
                    //如果时间存在回拨情况则等待5ms
                    Thread.sleep(5);
                } catch (InterruptedException ex) {
                    throw new UnsupportedOperationException(ex.getMessage(), ex);
                }
                timestamp = currentTimeMillis();
                if (timestamp < lastTimestamp) {
                    throw new RuntimeException("timestamp<lastTimestamp");
                }
            } else {
                throw new RuntimeException("timestamp<lastTimestamp");
            }
        }
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0L) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = getInitValue();
        }
        lastTimestamp = timestamp;
        // 最后按照规则拼出ID。
        // 000000000000000000000000000000000000000000               0000                             000000
        //          000000000000
        //                 time                                   dataCenterId                       workerId
        //            sequence
        return ((timestamp - epoch) << timestampLeftShift) | (dataCenterId << dataCenterIdShift) | (workerId <<
                workerIdShift) | sequence;
    }

    protected final long tilNextMillis(long lastTimestamp) {
        long timestamp = currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = currentTimeMillis();
        }
        return timestamp;
    }

    private final long currentTimeMillis() {
        //此方法有性能问题，有待更一步优化
        return System.currentTimeMillis();
    }

    public final long getInitValue() {
        return RANDOM.nextInt(10);
    }

    /**
     * @param bizId
     * @param dataCenterId
     */
    private void validCheck(final long dataCenterId, final long bizId) {
        if (bizId > maxWorkerId || bizId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }

        if (dataCenterId > maxDataCenterId || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("dataCenter Id can't be greater than %d or less than 0", maxDataCenterId));
        }
    }

    public void setWorkerId(long workerId) {
        this.workerId = workerId;
    }

    public void setDataCenterId(long dataCenterId) {
        this.dataCenterId = dataCenterId;
    }

    @Override
    public ModeEnum getType() {
        return ModeEnum.SNOW_FLAKE;
    }

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
        //148748591769460758
        Sequence sequence = new SnowflakeSequence();
        System.out.println(sequence.nextId("f"));
    }
}