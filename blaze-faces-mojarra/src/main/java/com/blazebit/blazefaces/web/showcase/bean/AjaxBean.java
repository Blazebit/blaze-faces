/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.web.showcase.bean;

import java.io.Serializable;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Christian Beikov
 */
@ManagedBean
@ViewScoped
public class AjaxBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public String getTime() {
        return new Date().toString();
    }
}
