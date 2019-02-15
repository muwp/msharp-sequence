package com.ruijing.sequence.service.impl;

import com.ruijing.sequence.api.remote.RemoteResponse;
import com.ruijing.fundamental.cat.Cat;
import com.ruijing.fundamental.cat.message.Transaction;
import com.ruijing.sequence.api.service.IdGenerator;
import com.ruijing.sequence.manager.SequenceConfigManager;
import com.ruijing.sequence.model.SequenceConfig;
import com.ruijing.sequence.service.Sequence;

import java.util.HashMap;
import java.util.Map;

/**
 * 唯一id生成器(全局id生成器)
 * https://gitee.com/robertleepeak/vesta-id-generator
 * https://gitee.com/mirrors/tinyid
 *
 * @author mwup
 * @version 1.0
 * @created 2017/4/21 00:13
 **/
public class IdGeneratorImpl implements IdGenerator {

    private static final String CAT_TYPE = "IdGenerator";

    private static final String CAT_NAME = "nextId";

    private Map<String, Sequence> sequenceMap = new HashMap<>();

    private SequenceConfigManager manager;

    @Override
    public RemoteResponse<Long> nextId(final String bizName, final String token) {
        final Transaction transaction = Cat.newTransaction(CAT_TYPE, CAT_NAME);
        transaction.addData("bizName:" + bizName);
        RemoteResponse<Long> response;
        try {
            response = execute(bizName, token);
            transaction.setSuccess();
        } catch (Exception ex) {
            transaction.setStatus(ex);
            response = RemoteResponse
                    .custom()
                    .setFailure(ex.getMessage())
                    .build();
        } finally {
            transaction.complete();
        }
        return response;
    }

    private RemoteResponse<Long> execute(final String bizName, final String token) {
        final SequenceConfig sequenceConfig = this.manager.load(bizName);
        if (null == sequenceConfig) {
            return RemoteResponse.custom().setFailure(new StringBuffer().append("bizName[").append(bizName).append("] unregistered,").append(" 相关问题QA: https://shimo.im/folder/YEO5OPjB0i8l98WZ").toString()).build();
        }

        if (!sequenceConfig.getToken().equals(token)) {
            return RemoteResponse.custom().setFailure(new StringBuffer().append("bizName[").append(bizName).append("] no authority,").append(" 相关问题QA: https://shimo.im/folder/YEO5OPjB0i8l98WZ").toString()).build();
        }

        final Sequence sequence = this.sequenceMap.get(sequenceConfig.getMode());
        if (null == sequence) {
            return RemoteResponse.custom().setFailure(new StringBuffer().append("sequence not exists,").append(" 相关问题QA: https://shimo.im/folder/YEO5OPjB0i8l98WZ").toString()).build();
        }
        final Long result = sequence.nextId(bizName);
        return RemoteResponse.custom().setData(result).setSuccess().build();
    }

    public void setSequences(Sequence... sequences) {
        for (int i = 0, size = sequences.length; i < size; i++) {
            sequenceMap.put(sequences[i].getType().getName(), sequences[i]);
        }
    }

    public void setManager(SequenceConfigManager manager) {
        this.manager = manager;
    }
}