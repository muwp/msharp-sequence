package com.ruijing.sequence.dao;

import com.ruijing.sequence.model.Sequence;

import java.util.List;

/**
 * SequenceDao
 *
 * @author mwup
 * @version 1.0
 * @created 2019/02/13 13:51
 **/
public interface SequenceDao {

    /**
     * 插入数据区间
     *
     * @param name          区间名称
     * @param initStepValue 初始位置
     */
    int insert(final String name, final long initStepValue);

    /**
     * 更新区间，乐观策略
     *
     * @param newValue 更新新数据
     * @param oldValue 更新旧数据
     * @param name     区间名称
     * @return 成功/失败
     */
    boolean updateRange(final Long newValue, final Long oldValue, final String name);

    /**
     * 查询区间，如果区间不存在，会新增一个区间，并返回null，由上层重新执行
     *
     * @param name      区间名称
     * @param stepStart 初始位置
     * @return 区间值
     */
    Long selectRange(final String name, final long stepStart);

    /**
     * 更新区间，乐观策略
     *
     * @param sequence 更新新数据
     * @return 成功/失败
     */
    int update(Sequence sequence);

    /**
     * 查询区间，如果区间不存在，会新增一个区间，并返回null，由上层重新执行
     *
     * @param name 区间名称
     * @return 区间值
     */
    List<Sequence> queryForList(final String name);

    /**
     * 查询区间，如果区间不存在，会新增一个区间，并返回null，由上层重新执行
     *
     * @param bizName  业务名称
     * @param index    页码偏移量
     * @param pageSize 每页的大小
     * @return 区间值
     */
    List<Sequence> queryForList(final String bizName, final int index, final int pageSize);
}
