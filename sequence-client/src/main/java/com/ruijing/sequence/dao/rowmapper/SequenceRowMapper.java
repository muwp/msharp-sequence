package com.ruijing.sequence.dao.rowmapper;

import com.ruijing.sequence.model.Sequence;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * SequenceRowMapper
 *
 * @author mwup
 * @version 1.0
 * @created 2019/02/15 13:51
 **/
public class SequenceRowMapper implements RowMapper<Sequence> {

    public static final RowMapper<Sequence> INSTANCE = new SequenceRowMapper();

    public static RowMapper<Sequence> getInstance() {
        return INSTANCE;
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
