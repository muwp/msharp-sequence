package com.ruijing.sequence.builder;

import com.ruijing.sequence.dao.impl.SequenceConfigDaoImpl;
import com.ruijing.sequence.dao.impl.SequenceDaoImpl;
import com.ruijing.sequence.manager.DbSeqRangeManager;
import com.ruijing.sequence.manager.SequenceConfigManager;
import com.ruijing.sequence.sequence.db.DbRangeSequence;
import com.ruijing.sequence.service.Sequence;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * 基于DB取步长，序列号生成器构建者
 *
 * @author mwup
 * @version 1.0
 * @created 2019/02/13 13:51
 **/
public class DbSeqBuilder implements SeqBuilder {

    /**
     * 数据库数据源[必选]
     */
    private DataSource dataSource;

    /**
     * 存放序列号步长的表[可选：默认：sequence]
     */
    private String tableName = StringUtils.EMPTY;

    @Override
    public Sequence build() {
        //利用DB获取区间管理器
        final DbSeqRangeManager dbSeqRangeManager = new DbSeqRangeManager();
        dbSeqRangeManager.setTableName(this.tableName);
        final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        final SequenceDaoImpl sequenceDao = new SequenceDaoImpl();
        sequenceDao.setJdbcTemplate(jdbcTemplate);
        dbSeqRangeManager.setSequenceDao(sequenceDao);

        final SequenceConfigDaoImpl sequenceConfigDao = new SequenceConfigDaoImpl();
        sequenceConfigDao.setJdbcTemplate(jdbcTemplate);
        SequenceConfigManager configManager = new SequenceConfigManager();
        configManager.setSequenceConfigDao(sequenceConfigDao);
        dbSeqRangeManager.setConfigManager(configManager);

        //构建序列号生成器
        final DbRangeSequence sequence = new DbRangeSequence();
        sequence.setSeqRangeManager(dbSeqRangeManager);
        return sequence;
    }

    public static DbSeqBuilder custom() {
        final DbSeqBuilder builder = new DbSeqBuilder();
        return builder;
    }

    public DbSeqBuilder dataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    public DbSeqBuilder tableName(String tableName) {
        this.tableName = tableName;
        return this;
    }
}
