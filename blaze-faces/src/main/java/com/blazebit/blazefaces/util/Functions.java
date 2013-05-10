/*
 * Copyright 2011-2012 Blazebit
 */
package com.blazebit.blazefaces.util;

import com.blazebit.blazefaces.apt.JsfFunction;

/**
 *
 * @author Christian Beikov
 */
public class Functions {

	@JsfFunction
    public static String concat(String s1, String s2) {
        return s1.concat(s2);
    }
}
