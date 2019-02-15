package com.ruijing.sequence.enums;

/**
 * id生成器模式
 *
 * @author mwup
 * @version 1.0
 * @created 2019/02/14 15:51
 **/
public enum ModeEnum {

    /**
     * DB mode
     */
    DB(1, "db"),

    /**
     * redis mode
     */
    @Deprecated
    REDIS(2, "redis"),

    /**
     * snowflake mode
     */
    SNOW_FLAKE(3, "snowflake");

    private final int code;

    private final String name;

    ModeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ModeEnum{");
        sb.append("code=").append(code);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
