package com.blazebit.blazefaces.apt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.render.Renderer;
import javax.faces.view.facelets.ComponentHandler;

/**
 *
 * @author Christian
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsfComponent {
    String tag() default "";
    String type() default "";
    String family() default "";
    JsfDescription description() default @JsfDescription;
    JsfAttribute[] attributes() default {};
    Class<? extends UIComponentBase> parent() default UIComponentBase.class;
    Class<? extends ComponentHandler> handler() default ComponentHandler.class;
    Class<? extends ClientBehavior> behavior() default ClientBehavior.class;
    Class<? extends Renderer> renderer() default Renderer.class;
}
