package com.ruijing.sequence.service;

import com.ruijing.sequence.enums.TypeEnum;

/**
 * 唯一id生成器(全局id生成器)
 * https://gitee.com/mirrors/tinyid
 *
 * @author mwup
 * @version 1.0
 * @created 2017/4/21 00:13
 **/
public interface Sequence {

    /**
     * 获取全局唯一的id号
     *
     * @param bizName
     * @return 唯一id号
     */
    Long nextId(String bizName);

    /**
     * 获取序列化模式
     *
     * @return {@link TypeEnum}
     */
    TypeEnum getType();
}
