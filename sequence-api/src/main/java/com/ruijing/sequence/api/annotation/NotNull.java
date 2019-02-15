package com.ruijing.sequence.api.annotation;

import java.lang.annotation.*;

/**
 * not null
 * <p>
 * Created by mwup on 2019/01/29.
 */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotNull {

    /**
     * @return
     */
    String value() default "";
}
