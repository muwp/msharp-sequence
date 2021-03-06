package com.ruijing.sequence.api.annotation;

import java.lang.annotation.*;

/**
 * 是变量不能非空
 * <p>
 * Created by mwup on 2018/8/09.
 */
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotBlank {

    /**
     * @return
     */
    String value() default "";
}
