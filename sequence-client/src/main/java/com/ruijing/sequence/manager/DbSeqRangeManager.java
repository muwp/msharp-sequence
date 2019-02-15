package com.ruijing.sequence.manager;

import com.ruijing.sequence.dao.SequenceDao;
import com.ruijing.sequence.exception.SequenceException;
import com.ruijing.sequence.model.SeqRange;
import com.ruijing.sequence.model.SequenceConfig;

/**
 * DB区间管理器
 *
 * @author mwup
 * @version 1.0
 * @created 2019/02/13 13:51
 **/
public class DbSeqRangeManager {

    /**
     * 表名前缀，为防止数据库表名冲突，默认带上这个前缀
     */
    private final static String SEQUENCE_TABLE_NAME = "sequence_data";

    /**
     * 序列号配置管理
     */
    private SequenceConfigManager configManager;

    /**
     * SequenceDao
     */
    private SequenceDao sequenceDao;

    /**
     * 表名，默认range
     */
    private String tableName = "sequence";

    public SeqRange nextRange(final String bizName) throws SequenceException {
        Long oldValue;
        Long newValue;
        final SequenceConfig configDO = configManager.load(bizName);
        if (null == configDO) {
            throw new SequenceException("nextRange biz_name [" + bizName + "] unregistered, 相关问题QA: https://shimo.im/folder/YEO5OPjB0i8l98WZ");
        }

        for (int i = 0, retryTimes = configDO.getRetryTimes(); i < retryTimes; i++) {
            oldValue = this.sequenceDao.selectRange(bizName, configDO.getInitValue());
            if (null == oldValue) {
                //区间不存在，重试
                continue;
            }

            newValue = oldValue + configDO.getStep();

            if (this.sequenceDao.updateRange(newValue, oldValue, bizName)) {
                return new SeqRange(oldValue + 1, newValue);
            }
            //else 失败重试
        }

        throw new SequenceException("Retried too many times, retryTimes = " + configDO.getRetryTimes());
    }

    private String getRealTableName() {
        return SEQUENCE_TABLE_NAME;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public SequenceDao getSequenceDao() {
        return sequenceDao;
    }

    public void setSequenceDao(SequenceDao sequenceDao) {
        this.sequenceDao = sequenceDao;
    }

    public SequenceConfigManager getConfigManager() {
        return configManager;
    }

    public void setConfigManager(SequenceConfigManager configManager) {
        this.configManager = configManager;
    }
}
