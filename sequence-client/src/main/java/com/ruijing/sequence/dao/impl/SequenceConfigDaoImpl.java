package com.ruijing.sequence.dao.impl;


import com.ruijing.sequence.dao.SequenceConfigDao;
import com.ruijing.sequence.model.SequenceConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * SequenceConfigDaoImpl
 *
 * @author mwup
 * @version 1.0
 * @created 2019/02/13 13:51
 **/
public class SequenceConfigDaoImpl implements SequenceConfigDao, RowMapper<SequenceConfig> {

    private final static String SQL_SELECT_RANGE = "SELECT id,biz_name,model,type,init_value,step,retry_times,token,reset_time,update_time FROM sequence_config WHERE biz_name=?";

    private JdbcTemplate jdbcTemplate;

    @Override
    public int update(SequenceConfig config) {
        return jdbcTemplate.update("update sequence_config set reset_time=? WHERE id=?", new Object[]{config.getResetTime(), config.getId()});
    }

    /**
     * 查询区间，如果区间不存在，会新增一个区间，并返回null，由上层重新执行
     *
     * @param bizName 来源
     * @return 区间值
     */
    public List<SequenceConfig> query(String bizName) {
        return jdbcTemplate.query(SQL_SELECT_RANGE, new Object[]{bizName}, this);
    }

    @Override
    public List<SequenceConfig> queryForList(String bizName, int index, int pageSize) {
        index = index <= 0 ? 1 : index;
        index = (index - 1) * pageSize;
        Object[] args;
        final StringBuilder sql = new StringBuilder();
        if (StringUtils.isBlank(bizName)) {
            sql.append("select id,biz_name,model,type,init_value,step,retry_times,token,reset_time,update_time FROM sequence_config limit ?,?");
            args = new Object[]{index, pageSize};
        } else {
            sql.append("select id,biz_name,model,type,init_value,step,retry_times,token,reset_time,update_time FROM sequence_config WHERE biz_name=? limit ?,?");
            args = new Object[]{bizName, index, pageSize};
        }
        return jdbcTemplate.query(sql.toString(), args, this);
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public SequenceConfig mapRow(ResultSet rs, int rowNum) throws SQLException {
        final SequenceConfig sequenceConfig = new SequenceConfig();
        final Long id = rs.getLong(1);
        sequenceConfig.setId(id);
        final String name = rs.getString(2);
        sequenceConfig.setBizName(name);
        final String model = rs.getString(3);
        sequenceConfig.setMode(model);
        final int type = rs.getInt(4);
        sequenceConfig.setType(type);
        final Long initValue = rs.getLong(5);
        sequenceConfig.setInitValue(initValue);
        final int step = rs.getInt(6);
        sequenceConfig.setStep(step);
        final int retryTimes = rs.getInt(7);
        sequenceConfig.setRetryTimes(retryTimes);
        final String token = rs.getString(8);
        sequenceConfig.setToken(token);
        final Date resetDate = rs.getTimestamp(9);
        sequenceConfig.setResetTime(resetDate);
        final Date updateTime = rs.getTimestamp(10);
        sequenceConfig.setUpdateTime(updateTime);
        return sequenceConfig;
    }
}
