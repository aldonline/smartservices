package com.linktegration.smartservices.tests.globalservice;

import com.linktegration.smartservices.Service;

public class NonGlobalService extends Service<Integer> {

	private static int executionCount = 0;

	private static synchronized void increaseExecutionCount() {
		executionCount++;
	}

	public static int getExecutionCount() {
		return executionCount;
	}

	private static boolean semaphore = false;

	public static synchronized void greenLight() {
		semaphore = true;
	}

	/* not specifying should default to false
	@Override
	protected boolean isGlobal() {
		return true;
	}
	*/

	@Override
	protected Integer execute() throws Exception {
		increaseExecutionCount();
		// we should not return right away
		// we should wait for green light
		// this way the test can create more tan one service instance
		// before we are removed from the global cache
		while (!semaphore) {
			Thread.sleep(100);
		}
		return 5;
	}

}
