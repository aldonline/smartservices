package com.linktegration.smartservices.tests.basic;

import java.util.concurrent.ExecutionException;

import com.linktegration.smartservices.SimpleRequest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class BasicServiceInvocationTest extends TestCase {
	public BasicServiceInvocationTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(BasicServiceInvocationTest.class);
	}

	public void testServiceMustReturnRequest() {
		assertNotNull("Service.request() must return a SimpleRequest",
				new BasicService().request());
	}

	public void testRequestMustReturnValue() throws InterruptedException,
			ExecutionException {
		BasicService ss = new BasicService();
		SimpleRequest<Integer> fi = ss.request();
		Integer i = fi.get();
		assertEquals(new Integer(5), i);
	}
}
