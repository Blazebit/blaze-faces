/*
 * Copyright 2011 Blazebit
 */
package com.blazebit.blazefaces.examples.view;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;

import com.blazebit.blazefaces.util.RendererUtils;

/**
 * 
 * @author Christian Beikov
 */
@Named
@RequestScoped
public class SemanticBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date yesterday;

	public SemanticBean() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -1);
		yesterday = new Date(c.getTimeInMillis());
	}

	public Date getYesterday() {
		return yesterday;
	}

	public void setYesterday(Date yesterday) {
		this.yesterday = yesterday;
	}

	public String getSanatized() throws ScanException, PolicyException,
			MalformedURLException {
		String bad = "<div><p>This <mark>is</mark> a sample<script>alert('Bad')</script></p></div>";
		AntiSamy as = new AntiSamy();
		CleanResults cr = as.scan(bad, RendererUtils.getPolicy());
		String clean = cr.getCleanHTML();
		return clean;
	}
}
