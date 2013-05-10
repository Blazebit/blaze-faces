
package com.blazebit.blazefaces.apt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 *
 * @author Christian
 */
@Target({ElementType.METHOD})
public @interface JsfAttribute {
    String name() default "";
    JsfDescription description() default @JsfDescription;
    boolean required() default false;
    Class<?> type() default String.class;
    boolean ignore() default false;
    String defaultValue() default "";
}
