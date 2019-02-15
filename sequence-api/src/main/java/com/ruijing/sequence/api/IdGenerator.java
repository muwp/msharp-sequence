package com.ruijing.sequence.api;

import com.ruijing.fundamental.api.annotation.NotBlank;
import com.ruijing.fundamental.api.remote.RemoteResponse;

/**
 * 锐竞全局统一id生成器
 *
 * @author mwup
 * @version 1.0
 * @created 2019/02/14 00:13
 **/
public interface IdGenerator {

    /**
     * 获取全局唯一的id号
     *
     * @param bizName 业务号
     *                注:业务号需要接入方向平台申请,相关文档: https://shimo.im/docs/Afmz7hs5EsEWabk6
     * @param token   用于客户端业务鉴权的token,需要向平台申请
     * @return 唯一id号
     */
    RemoteResponse<Long> nextId(@NotBlank String bizName, @NotBlank String token);
}
