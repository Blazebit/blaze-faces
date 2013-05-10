
package com.blazebit.blazefaces.apt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import javax.faces.event.FacesEvent;
import javax.faces.event.SystemEvent;

/**
 *
 * @author Christian
 */
@Target({ElementType.TYPE})
public @interface JsfSystemEventListeners {
	JsfSystemEventListener[] value();
}
