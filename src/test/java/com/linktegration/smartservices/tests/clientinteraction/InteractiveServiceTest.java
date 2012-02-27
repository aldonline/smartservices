package com.linktegration.smartservices.tests.clientinteraction;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.linktegration.smartservices.InteractiveRequest;
import com.linktegration.smartservices.util.InteractiveResponse;

public class InteractiveServiceTest extends TestCase {
	public InteractiveServiceTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(InteractiveServiceTest.class);
	}

	public void test1() throws Exception {

		InteractiveService service = new InteractiveService();
		InteractiveRequest<String> request = service.interactiveRequest();

		assertNotNull(
				"Service.interactiveRequest() must return an InteractiveRequest",
				request);

		InteractiveResponse<String> res;

		res = request.get();

		assertNotNull(
				"InteractiveRequest.get() must return an InteractiveResponse",
				res);

		assertFalse(res.isResult());
		assertEquals("name", res.getMessage());
		request.answer("Aldo");

		res = request.get();
		assertFalse(res.isResult());
		assertEquals("lastname", res.getMessage());
		request.answer("Bucchi");

		res = request.get();
		assertTrue(res.isResult());
		assertEquals("Aldo Bucchi", res.getValue());
	}
}
