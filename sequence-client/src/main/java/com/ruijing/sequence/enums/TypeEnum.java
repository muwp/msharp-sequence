package com.ruijing.sequence.enums;

/**
 * 序号循环重置类型
 * <p>
 * TypeEnum
 *
 * @author mwup
 * @version 1.0
 * @created 2019/02/15 15:51
 **/
public enum TypeEnum {

    /**
     * none type
     */
    NONE(0, "none"),

    /**
     * minute type
     */
    MINUTE(1, "minute"),

    /**
     * hour type
     */
    HOUR(2, "hour"),

    /**
     * day type
     */
    DAY(3, "day"),

    /**
     * month type
     */
    MONTH(4, "month"),

    /**
     * year type
     */
    YEAR(5, "year");

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
        final StringBuilder sb = new StringBuilder("ModeEnum{");
        sb.append("code=").append(code);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
