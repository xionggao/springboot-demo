package com.xxy.sfl.pub.annotations;

import java.lang.annotation.*;

/**
 * 显示的中文名称
 *
 * @author xg
 */
@Target(value = {ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Display {
    String value() default "";
}