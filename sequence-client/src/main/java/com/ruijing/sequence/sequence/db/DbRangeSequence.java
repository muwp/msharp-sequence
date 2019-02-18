package com.ruijing.sequence.sequence.db;

import com.ruijing.sequence.enums.ModeEnum;
import com.ruijing.sequence.exception.SequenceException;
import com.ruijing.sequence.manager.DbSeqRangeManager;
import com.ruijing.sequence.model.SeqRange;
import com.ruijing.sequence.service.Sequence;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 序列号区间生成器接口默认实现
 *
 * @author mwup
 * @version 1.0
 * @created 2019/02/13 13:51
 **/
public class DbRangeSequence implements Sequence {

    /**
     * 序列号区间管理器
     */
    private volatile DbSeqRangeManager seqRangeManager;

    /**
     * 当前序列号区间
     */
    private static volatile Map<String, SeqRange> seqRangeMap = new ConcurrentHashMap<>();

    @Override
    public Long nextId(final String bizName) {
        SeqRange seqRange = load(bizName);

        //当value值为-1时，表明区间的序列号已经分配完，需要重新获取区间
        long value = seqRange.getAndIncrement();
        if (value == -1) {
            synchronized (bizName) {
                for (; ; ) {
                    seqRange = seqRangeMap.get(bizName);
                    if (null == seqRange) {
                        continue;
                    }
                    value = seqRange.getAndIncrement();
                    if (value != -1) {
                        break;
                    }
                    seqRange = this.seqRangeManager.nextRange(bizName);
                    seqRangeMap.put(bizName, seqRange);
                    value = seqRange.getAndIncrement();
                    if (value != -1) {
                        break;
                    }
                }
            }
        }

        if (value < 0) {
            throw new SequenceException("id value overflow, value = " + value);
        }

        return value;
    }

    private SeqRange load(final String bizName) {
        SeqRange seqRange = seqRangeMap.get(bizName);
        //当前区间不存在，重新获取一个区间
        if (null == seqRange) {
            synchronized (bizName) {
                seqRange = seqRangeMap.get(bizName);
                if (null == seqRange) {
                    seqRange = this.seqRangeManager.nextRange(bizName);
                    seqRangeMap.put(bizName, seqRange);
                }
            }
        }
        return seqRange;
    }

    public void setSeqRangeManager(DbSeqRangeManager seqRangeMgr) {
        this.seqRangeManager = seqRangeMgr;
    }

    @Override
    public ModeEnum getType() {
        return ModeEnum.DB;
    }

    public static void resetSeqRange(final String bizName) {
        if (seqRangeMap.containsKey(bizName)) {
            seqRangeMap.remove(bizName);
        }
    }
}
