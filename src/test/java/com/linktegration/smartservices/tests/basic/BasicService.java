package com.linktegration.smartservices.tests.basic;

import com.linktegration.smartservices.Service;

public class BasicService extends Service<Integer> {

	@Override
	protected Integer execute() throws Exception {
		return 5;
	}

}
