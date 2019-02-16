package com.ruijing.sequence.dao.rowmapper;

import com.ruijing.sequence.model.SequenceConfig;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * SequenceConfigRowMapper
 *
 * @author mwup
 * @version 1.0
 * @created 2019/02/15 13:51
 **/
public class SequenceConfigRowMapper implements RowMapper<SequenceConfig> {

    public static final RowMapper<SequenceConfig> INSTANCE = new SequenceConfigRowMapper();

    public static RowMapper<SequenceConfig> getInstance() {
        return INSTANCE;
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
