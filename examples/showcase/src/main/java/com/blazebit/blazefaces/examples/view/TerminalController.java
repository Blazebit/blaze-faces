package com.blazebit.blazefaces.examples.view;

import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class TerminalController {

	public String handleCommand(String command, String[] params) {
		if (command.equals("greet"))
			return "Hello " + params[0];
		else if (command.equals("date"))
			return new Date().toString();
		else
			return command + " not found";
	}
}
