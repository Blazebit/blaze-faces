/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.util;

import javax.faces.context.FacesContext;

public class AgentUtil {

    private AgentUtil() {
    }

    public static boolean isIE(FacesContext facesContext) {
        String userAgent = facesContext.getExternalContext().getRequestHeaderMap().get("User-Agent");

        if (userAgent == null) {
            return false;
        }

        return (userAgent.indexOf("MSIE") != -1);
    }
}