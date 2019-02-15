package com.ruijing.sequence.dao.impl;

import com.ruijing.sequence.dao.SequenceDao;
import com.ruijing.sequence.exception.SequenceException;
import com.ruijing.sequence.model.Sequence;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * SequenceDaoImpl
 *
 * @author mwup
 * @version 1.0
 * @created 2019/02/13 13:51
 **/
public class SequenceDaoImpl implements SequenceDao, RowMapper<Sequence> {

    private static final long DELTA = 100000000L;

    private final static String SQL_INSERT_RANGE = "INSERT IGNORE INTO sequence_data (biz_name,max_id,update_time) VALUE(?,?,?)";

    private final static String SQL_UPDATE_RANGE = "UPDATE sequence_data SET max_id=? WHERE biz_name=? AND max_id=?";

    private final static String SQL_SELECT_RANGE = "SELECT max_id FROM sequence_data WHERE biz_name=?";

    private final static String SQL_QUERY_RANGE = "SELECT id,biz_name,max_id,update_time FROM sequence_data WHERE biz_name=?";

    private JdbcTemplate jdbcTemplate;

    @Override
    public int insert(final String name, final long initStepValue) {
        return jdbcTemplate.update(SQL_INSERT_RANGE, new Object[]{name, initStepValue, new Timestamp(System.currentTimeMillis())});
    }

    @Override
    public boolean updateRange(final Long newValue, final Long oldValue, final String name) {
        int affectedRows = jdbcTemplate.update(SQL_UPDATE_RANGE, new Object[]{newValue, name, oldValue});
        return affectedRows > 0;
    }

    @Override
    public Long selectRange(final String bizName, final long stepStart) {
        long oldValue;
        final List<Long> list = jdbcTemplate.queryForList(SQL_SELECT_RANGE, new Object[]{bizName}, Long.class);
        if (CollectionUtils.isEmpty(list)) {
            //没有此类型数据，需要初始化
            insert(bizName, stepStart);
            return null;
        }
        oldValue = list.get(0);
        if (oldValue < 0) {
            String msg = "snowflake generator max_id cannot be less than zero, max_id = " + oldValue + ", please check table snowflake generator";
            throw new SequenceException(msg);
        }

        if (oldValue > Long.MAX_VALUE - DELTA) {
            String msg = "snowflake generator max_id overflow, max_id = " + oldValue + ", please check table snowflake generator";
            throw new SequenceException(msg);
        }
        return oldValue;
    }

    @Override
    public int update(final Sequence sequence) {
        return jdbcTemplate.update("UPDATE sequence_data SET max_id=? WHERE id=?", new Object[]{sequence.getMaxId(), sequence.getId()});
    }

    @Override
    public List<Sequence> queryForList(final String bizName, int index, final int pageSize) {
        final StringBuilder sql = new StringBuilder();
        index = index <= 0 ? 1 : index;
        index = (index - 1) * pageSize;
        Object[] args;
        if (StringUtils.isBlank(bizName)) {
            sql.append("select id,biz_name,max_id,update_time from  sequence_data limit ?,?");
            args = new Object[]{index, pageSize};
        } else {
            sql.append("select id,biz_name,max_id,update_time from sequence_data where biz_name=? limit ?,?");
            args = new Object[]{bizName, index, pageSize};
        }
        return jdbcTemplate.query(sql.toString(), args, this);
    }

    @Override
    public List<Sequence> queryForList(String bizName) {
        return jdbcTemplate.query("select id,biz_name,max_id,update_time from sequence_data where biz_name=?", new Object[]{bizName}, this);
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Sequence mapRow(ResultSet rs, int rowNum) throws SQLException {
        final Sequence sequence = new Sequence();
        final Long id = rs.getLong(1);
        sequence.setId(id);
        final long maxId = rs.getLong(3);
        sequence.setMaxId(maxId);
        final String name = rs.getString(2);
        sequence.setBizName(name);
        final Date updateTime = rs.getTimestamp(4);
        sequence.setUpdateTime(updateTime);
        return sequence;
    }
}
