package com.ruijing.sequence.builder;

import com.ruijing.sequence.service.Sequence;

/**
 * 序列号生成器构建者
 *
 * @author mwup
 * @version 1.0
 * @created 2019/02/13 13:51
 **/
public interface SeqBuilder {

    /**
     * 构建一个序列号生成器
     *
     * @return 序列号生成器
     */
    Sequence build();
}
