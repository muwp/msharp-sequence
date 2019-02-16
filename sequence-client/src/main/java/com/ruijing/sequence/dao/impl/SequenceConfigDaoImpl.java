package com.ruijing.sequence.dao.impl;

import com.ruijing.sequence.dao.SequenceConfigDao;
import com.ruijing.sequence.dao.rowmapper.SequenceConfigRowMapper;
import com.ruijing.sequence.jdbc.single.SimpleJdbcTemplate;
import com.ruijing.sequence.model.SequenceConfig;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * SequenceConfigDaoImpl
 *
 * @author mwup
 * @version 1.0
 * @created 2019/02/13 13:51
 **/
public class SequenceConfigDaoImpl implements SequenceConfigDao {

    private final static String SQL_SELECT_RANGE = "SELECT id,biz_name,mode,type,init_value,step,retry_times,token,reset_time,update_time FROM sequence_config WHERE biz_name=?";

    private final static String SQL_SELECT_BIZ_NAME_QUERY = "select id,biz_name,mode,type,init_value,step,retry_times,token,reset_time,update_time FROM sequence_config WHERE biz_name=? limit ?,?";

    private final static String SQL_SELECT_NOT_BIZ_NAME_QUERY = "select id,biz_name,mode,type,init_value,step,retry_times,token,reset_time,update_time FROM sequence_config limit ?,?";

    private SimpleJdbcTemplate jdbcTemplate;

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
    @Override
    public List<SequenceConfig> query(String bizName) {
        return jdbcTemplate.query(SQL_SELECT_RANGE, new Object[]{bizName}, SequenceConfigRowMapper.getInstance());
    }

    @Override
    public List<SequenceConfig> queryForList(String bizName, int index, int pageSize) {
        index = index <= 0 ? 1 : index;
        index = (index - 1) * pageSize;
        Object[] args;
        final StringBuilder sql = new StringBuilder();
        if (StringUtils.isBlank(bizName)) {
            sql.append(SQL_SELECT_NOT_BIZ_NAME_QUERY);
            args = new Object[]{index, pageSize};
        } else {
            sql.append(SQL_SELECT_BIZ_NAME_QUERY);
            args = new Object[]{bizName, index, pageSize};
        }
        return jdbcTemplate.query(sql.toString(), args, SequenceConfigRowMapper.getInstance());
    }

    public void setJdbcTemplate(SimpleJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
