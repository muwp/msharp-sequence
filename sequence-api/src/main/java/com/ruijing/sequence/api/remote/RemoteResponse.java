package com.ruijing.sequence.api.remote;

import java.io.Serializable;

/**
 * rpc接口返回值数据结构
 *
 * @author mwup
 * @version 1.0
 * @created 2018/06/12 17:15
 **/
public class RemoteResponse<T> implements Serializable {

    /**
     * 序列化uid
     */
    public static final long serialVersionUID = 586262417756505439L;

    /**
     * 成功状态码
     */
    public static final int SUCCESS = 200;

    /**
     * 失败状态码
     */
    public static final int FAILURE = 500;

    /**
     * 内部异常状态码
     */
    public static final int EXCEPTION = 400;

    /**
     * 内部业务鉴权异常
     */
    public static final int UNAUTHORIZED = 401;

    private static final String SUCCESS_INFO = "success";

    /**
     * 返回值状态码
     * {@link RemoteResponse#SUCCESS}
     * {@link RemoteResponse#FAILURE}
     * {@link RemoteResponse#EXCEPTION}
     * {@link RemoteResponse#UNAUTHORIZED}
     */
    private int code;

    /**
     * 返回结果描述信息
     */
    private String msg;

    /**
     * 返回结果数据
     */
    private T data;

    public RemoteResponse() {
    }

    public RemoteResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private RemoteResponse(final Builder<T> builder) {
        setCode(builder.code);
        setMsg(builder.msg);
        setData(builder.data);
    }

    public static Builder custom() {
        return new Builder();
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return this.code == SUCCESS;
    }

    @Override
    public String toString() {
        return "RemoteResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public static RemoteResponse success() {
        final RemoteResponse remoteResponse = new RemoteResponse();
        remoteResponse.code = SUCCESS;
        remoteResponse.msg = SUCCESS_INFO;
        remoteResponse.data = Boolean.TRUE;
        return remoteResponse;
    }

    public RemoteResponse failure(String msg) {
        this.code = FAILURE;
        this.msg = msg;
        return this;
    }

    public RemoteResponse failure() {
        this.code = FAILURE;
        this.msg = "server error";
        return this;
    }

    public static final class Builder<T> {

        /**
         * response code
         */
        private int code;

        /**
         * response msg
         */
        private String msg;

        /**
         * response data
         */
        private T data;

        private Builder() {
        }

        public Builder setCode(int val) {
            code = val;
            return this;
        }

        public Builder setSuccess() {
            code = SUCCESS;
            msg = SUCCESS_INFO;
            return this;
        }

        public Builder setFailure(String msg) {
            code = FAILURE;
            this.msg = msg;
            return this;
        }

        public Builder setException(String msg) {
            code = EXCEPTION;
            this.msg = msg;
            return this;
        }

        public Builder setMsg(String val) {
            msg = val;
            return this;
        }

        public Builder setData(T val) {
            data = val;
            return this;
        }

        public RemoteResponse build() {
            return new RemoteResponse(this);
        }
    }
}
