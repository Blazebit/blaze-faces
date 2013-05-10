
package com.blazebit.blazefaces.apt;

/**
 *
 * @author Christian
 */
public @interface JsfDescription {
    String displayName() default "";
    String description() default "";
    String smallIcon() default "";
    String largeIcon() default "";
}
