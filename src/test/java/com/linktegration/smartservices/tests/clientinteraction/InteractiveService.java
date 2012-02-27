package com.linktegration.smartservices.tests.clientinteraction;

import com.linktegration.smartservices.Service;

public class InteractiveService extends Service<String> {
	@Override
	protected String execute() throws Exception {
		String name = (String) interact("name");
		String lastname = (String) interact("lastname");
		return name + " " + lastname;
	}
}