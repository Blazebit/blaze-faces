
package com.blazebit.blazefaces.apt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 *
 * @author Christian
 */
@Target(ElementType.METHOD)
public @interface JsfFunction {
    String name() default "";
    JsfDescription description() default @JsfDescription;
}
