package com.linktegration.smartservices.tests.clientinteraction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.linktegration.smartservices.Service;

public class InteractiveService extends Service<String> {
	
	private Log log = LogFactory.getLog(InteractiveService.class);		
	
	@Override
	protected String execute() throws Exception {
		log.debug("execute()");
		log.debug("execute() asking for 'name'");
		String name = (String) interact("name");
		log.debug("asking for 'lastname'");
		String lastname = (String) interact("lastname");
		log.debug("returning result: " + name + " " + lastname);
		return name + " " + lastname;
	}
}