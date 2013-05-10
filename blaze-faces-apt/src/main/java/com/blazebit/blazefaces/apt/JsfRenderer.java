package com.blazebit.blazefaces.apt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 *
 * @author Christian
 */
@Target(ElementType.TYPE)
public @interface JsfRenderer {
    String type() default "";
    String family() default "";
}
