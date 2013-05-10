package com.blazebit.blazefaces.apt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 *
 * @author Christian
 */
@Target(ElementType.PACKAGE)
public @interface JsfNamespace {
	/**
	 * The name of the library.
	 * 
	 * @return
	 */
    // TODO: attach cbav global unique validator
    String name();
    /**
     * The URI to the library.
     * 
     * @return
     */
    // TODO: attach cbav url validator
    String uri();
    /**
     * The short name for the library.
     * 
     * @return
     */
    // TODO: attach cbav regex validator with [a-zA-Z].*
    String shortName();
}
