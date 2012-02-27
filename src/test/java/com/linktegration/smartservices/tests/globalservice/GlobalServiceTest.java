package com.linktegration.smartservices.tests.globalservice;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.linktegration.smartservices.SimpleRequest;

public class GlobalServiceTest extends TestCase {
	public GlobalServiceTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(GlobalServiceTest.class);
	}

	public void testNonGlobal1() throws Exception {

		NonGlobalService service1 = new NonGlobalService();
		NonGlobalService service2 = new NonGlobalService();

		// now lets start testing

		SimpleRequest<Integer> req1 = service1.request();
		SimpleRequest<Integer> req2 = service2.request();

		// both requests should have been routed to the same service instance
		// meaning that service2 acted like a proxy

		// we now tell the executing service(s) that they may actually return
		// their value and thus finish executing
		NonGlobalService.greenLight();

		// and get the values from the two requests
		req1.get();
		req2.get();

		// two services executed
		assertEquals(2, NonGlobalService.getExecutionCount());

		// we now create a new instance and issue a request
		// and wait for the value to return

		new NonGlobalService().request().get();

		assertEquals(3, NonGlobalService.getExecutionCount());

	}	
	
	public void testGlobal1() throws Exception {

		GlobalService service1 = new GlobalService();
		GlobalService service2 = new GlobalService();

		// now lets start testing

		SimpleRequest<Integer> req1 = service1.request();
		SimpleRequest<Integer> req2 = service2.request();

		// both requests should have been routed to the same service instance
		// meaning that service2 acted like a proxy

		// we now tell the executing service(s) that they may actually return
		// their value and thus finish executing
		GlobalService.greenLight();

		// and get the values from the two requests
		req1.get();
		req2.get();

		// only one service executed
		assertEquals(1, GlobalService.getExecutionCount());

		// we now create a new instance and issue a request
		// and wait for the value to return

		new GlobalService().request().get();

		assertEquals(2, GlobalService.getExecutionCount());

	}
}
