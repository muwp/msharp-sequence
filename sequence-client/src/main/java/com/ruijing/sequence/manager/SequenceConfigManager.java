package com.ruijing.sequence.manager;

import com.ruijing.sequence.dao.SequenceConfigDao;
import com.ruijing.sequence.dao.SequenceDao;
import com.ruijing.sequence.enums.ModeEnum;
import com.ruijing.sequence.model.Sequence;
import com.ruijing.sequence.model.SequenceConfig;
import com.ruijing.sequence.sequence.db.DbRangeSequence;
import org.apache.commons.collections.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 唯一id生成器配置管理器
 *
 * @author mwup
 * @version 1.0
 * @created 2019/02/13 13:51
 **/
public class SequenceConfigManager implements Runnable {

    private SequenceConfigDao sequenceConfigDao;

    private SequenceDao sequenceDao;

    private Map<String, SequenceConfig> cache = new HashMap<>();

    private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName("sequence-check-idCache-thread");
            t.setDaemon(true);
            return t;
        }
    });

    public SequenceConfigManager() {
        this.service.scheduleAtFixedRate(this, 10, 60, TimeUnit.SECONDS);
    }

    public SequenceConfig load(final String bizName) {
        SequenceConfig configDO = cache.get(bizName);
        if (null != configDO) {
            return configDO;
        }
        configDO = loadSequenceConfig(bizName);
        return configDO;
    }

    private synchronized SequenceConfig loadSequenceConfig(final String bizName) {
        SequenceConfig configDO = this.cache.get(bizName);
        if (null != configDO) {
            return configDO;
        }
        final List<SequenceConfig> configDOList = this.sequenceConfigDao.query(bizName);
        if (CollectionUtils.isEmpty(configDOList)) {
            return null;
        }
        configDO = configDOList.get(0);
        this.cache.put(configDO.getBizName(), configDO);
        return configDO;
    }

    @Override
    public void run() {
        if (null == this.sequenceConfigDao) {
            return;
        }
        try {
            int index = 0;
            final int pageSize = 100;
            for (; ; ) {
                index++;
                final List<SequenceConfig> configs = sequenceConfigDao.queryForList(null, index, pageSize);
                if (CollectionUtils.isEmpty(configs)) {
                    break;
                }

                for (final SequenceConfig config : configs) {
                    if (config.getMode().equals(ModeEnum.SNOW_FLAKE)) {
                        continue;
                    }
                    final SequenceConfig old = this.cache.get(config.getBizName());
                    if (null == old) {
                        this.cache.put(config.getBizName(), config);
                    } else {
                        old.setId(config.getId());
                        old.setInitValue(config.getInitValue());
                        old.setRetryTimes(config.getRetryTimes());
                        old.setStep(config.getStep());
                        old.setType(config.getType());
                        old.setToken(config.getToken());
                        old.setUpdateTime(config.getUpdateTime());
                    }
                    resetSequenceData(config);
                    if (configs.size() < pageSize) {
                        break;
                    }
                }
            }
        } catch (Throwable ex) {
            //quite
            ex.printStackTrace();
        }
    }

    /**
     * 序列生成策略
     * 0 表示不循环重置
     * 1 按小时重置初始值
     * 2 按天重置初始值
     * 3 按月重置初始值
     * 4 按年重置初始值
     *
     * @param config
     */
    private void resetSequenceData(final SequenceConfig config) {
        try {
            final int type = config.getType();
            if (type == 0) {
                return;
            }
            final Date resetTime = config.getResetTime();
            if (null == resetTime) {
                return;
            }
            final Date now = new Date();
            if (type == 1 && now.getMinutes() != resetTime.getMinutes()) {
                updateData(config, now);
            } else if (type == 2 && now.getHours() != resetTime.getHours()) {
                updateData(config, now);
            } else if (type == 3 && now.getDay() != resetTime.getDay()) {
                updateData(config, now);
            } else if (type == 4 && now.getMonth() != resetTime.getMonth()) {
                updateData(config, now);
            } else if (type == 5 && now.getYear() != resetTime.getYear()) {
                updateData(config, now);
            }
        } catch (Throwable ex) {
            //quite
            ex.printStackTrace();
        }
    }

    private void updateData(final SequenceConfig config, final Date now) {
        final SequenceConfig newConfig = new SequenceConfig();
        newConfig.setId(config.getId());
        newConfig.setResetTime(now);
        final List<Sequence> sequences = this.sequenceDao.queryForList(config.getBizName());
        if (CollectionUtils.isEmpty(sequences)) {
            return;
        }
        //reset biz initValue
        DbRangeSequence.resetSeqRange(config.getBizName());
        for (final Sequence sequence : sequences) {
            if (config.getInitValue() == sequence.getMaxId()) {
                continue;
            }
            sequence.setMaxId(config.getInitValue());
            this.sequenceDao.update(sequence);
            this.sequenceConfigDao.update(newConfig);
        }
    }

    public void setSequenceConfigDao(SequenceConfigDao sequenceConfigDao) {
        this.sequenceConfigDao = sequenceConfigDao;
    }

    public void setSequenceDao(SequenceDao sequenceDao) {
        this.sequenceDao = sequenceDao;
    }
}