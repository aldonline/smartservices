package com.linktegration.smartservices.tests.clientinteraction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.linktegration.smartservices.Service;

public class AskUserForFullNameAndCapitalize extends Service<String> {
	
	private Log log = LogFactory.getLog(AskUserForFullNameAndCapitalize.class);		
	
	@Override
	protected String execute() throws Exception {
		
		Service<String> service = new AskUserForFullName();
		
		String fullname = service.request().get();
		
		return fullname.toUpperCase();
		
	}
}