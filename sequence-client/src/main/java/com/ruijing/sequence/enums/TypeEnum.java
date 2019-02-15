package com.ruijing.sequence.enums;

/**
 * id生成器模式
 *
 * @author mwup
 * @version 1.0
 * @created 2019/02/14 15:51
 **/
public enum TypeEnum {

    /**
     * DB type
     */
    DB(1, "db"),

    /**
     * redis type
     */
    REDIS(2, "redis"),

    /**
     * snowflake type
     */
    SNOW_FLAKE(3, "snowflake");

    private final int code;

    private final String name;

    TypeEnum(int code, String name) {
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
        final StringBuffer sb = new StringBuffer("TypeEnum{");
        sb.append("code=").append(code);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
