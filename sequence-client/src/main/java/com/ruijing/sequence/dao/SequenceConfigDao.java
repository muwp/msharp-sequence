package com.ruijing.sequence.dao;


import com.ruijing.sequence.model.SequenceConfig;

import java.util.List;

/**
 * SequenceConfigDao
 *
 * @author mwup
 * @version 1.0
 * @created 2019/02/13 13:51
 **/
public interface SequenceConfigDao {

    int update(SequenceConfig config);

    List<SequenceConfig> query(String bizName);

    /**
     * 查询区间，如果区间不存在，会新增一个区间，并返回null，由上层重新执行
     *
     * @param bizName  业务名称
     * @param index    页码偏移量
     * @param pageSize 每页的大小
     * @return 区间值
     */
    List<SequenceConfig> queryForList(final String bizName, final int index, final int pageSize);

}
