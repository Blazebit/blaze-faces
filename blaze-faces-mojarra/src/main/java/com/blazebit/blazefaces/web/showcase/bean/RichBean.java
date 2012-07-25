/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.web.showcase.bean;

import java.io.Serializable;
import java.net.MalformedURLException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;

import com.blazebit.blazefaces.util.RendererUtils;

/**
 *
 * @author Christian Beikov
 */
@ManagedBean
@RequestScoped
public class RichBean implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    public String getSanatized() throws ScanException, PolicyException, MalformedURLException{
        String bad = "<div><p>This <mark>is</mark> a sample<script>alert('Bad')</script></p></div>";
        AntiSamy as = new AntiSamy();
        CleanResults cr = as.scan(bad, RendererUtils.getPolicy());
        String clean = cr.getCleanHTML();
        return clean;
    }
}
