package com.blazebit.blazefaces.examples.application;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class CoerceToZeroServletContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.setProperty("org.apache.el.parser.COERCE_TO_ZERO", "false");
	}

}
