package com.blazebit.blazefaces.apt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorBase;
import javax.faces.component.behavior.ClientBehaviorHint;
import javax.faces.render.ClientBehaviorRenderer;
import javax.faces.view.facelets.BehaviorHandler;

/**
 *
 * @author Christian
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsfBehavior {
    String id() default "";
    String tag() default "";
    ClientBehaviorHint[] hints() default {};
    JsfDescription description() default @JsfDescription;
    JsfAttribute[] attributes() default {};
    Class<? extends ClientBehavior> parent() default ClientBehaviorBase.class;
    Class<? extends BehaviorHandler> handler() default BehaviorHandler.class;
    Class<? extends ClientBehaviorRenderer> renderer() default ClientBehaviorRenderer.class;
}
