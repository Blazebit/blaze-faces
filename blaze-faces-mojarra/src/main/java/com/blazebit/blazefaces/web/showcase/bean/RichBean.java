/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.web.showcase.bean;

import com.blazebit.blazefaces.util.RendererUtil;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;

/**
 *
 * @author Christian Beikov
 */
@ManagedBean
@RequestScoped
public class RichBean implements Serializable {
    
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
        CleanResults cr = as.scan(bad, RendererUtil.getPolicy());
        String clean = cr.getCleanHTML();
        return clean;
    }
}
