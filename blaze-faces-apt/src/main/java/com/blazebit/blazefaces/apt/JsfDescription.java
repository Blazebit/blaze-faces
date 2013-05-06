
package com.blazebit.blazefaces.apt;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author Christian
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface JsfDescription {
    String displayName() default "";
    String description() default "";
    String smallIcon() default "";
    String largeIcon() default "";
}
