package com.linktegration.smartservices.tests.clientinteraction;

import java.util.concurrent.Future;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.linktegration.smartservices.InteractiveRequest;
import com.linktegration.smartservices.InteractiveResponse;
import com.linktegration.smartservices.Service;

public class InteractiveServiceTest extends TestCase {

	private Log log = LogFactory.getLog(AskUserForFullName.class);

	public InteractiveServiceTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(InteractiveServiceTest.class);
	}

	public void testDirectInvocation() throws Exception {

		log.debug("testDirectInvocation()");

		Service<String> service = new AskUserForFullName();

		InteractiveRequest<String> request = service.interactiveRequest();

		assertNotNull(
				"Service.interactiveRequest() must return an InteractiveRequest",
				request);

		Future<InteractiveResponse<String>> future;
		InteractiveResponse<String> res;

		// step 1

		future = request.getFirst();

		assertNotNull(
				"InteractiveRequest.getFirst() must return a Future<InteractiveResponse<T>>",
				request);

		res = future.get();
		log.debug("testDirectInvocation() got response: " + res);
		System.out.println(res);
		assertFalse(res.isResult());
		assertEquals("name", res.getMessage());
		log.debug("testDirectInvocation() will answer first question");
		future = res.answerAndGetNext("Aldo");
		log.debug("testDirectInvocation() answered first question");

		assertNotNull(future);

		// step 2

		res = future.get();
		log.debug("testDirectInvocation() got response: " + res);
		assertFalse(res.isResult());
		assertEquals("lastname", res.getMessage());

		future = res.answerAndGetNext("Bucchi");

		// step 3

		res = future.get();
		log.debug("testDirectInvocation() got response: " + res);

		assertTrue(res.isResult());
		assertEquals("Aldo Bucchi", res.getValue());
	}

	public void testNestedInvocation() throws Exception {

		log.debug("testNestedInvocation()");

		Service<String> service = new AskUserForFullNameAndCapitalize();

		InteractiveRequest<String> request = service.interactiveRequest();

		assertNotNull(
				"Service.interactiveRequest() must return an InteractiveRequest",
				request);

		Future<InteractiveResponse<String>> future;
		InteractiveResponse<String> res;

		// step 1

		future = request.getFirst();

		assertNotNull(
				"InteractiveRequest.getFirst() must return a Future<InteractiveResponse<T>>",
				request);

		res = future.get();
		log.debug("testNestedInvocation() got response: " + res);
		System.out.println(res);
		assertFalse(res.isResult());
		assertEquals("name", res.getMessage());
		log.debug("testNestedInvocation() will answer first question");
		future = res.answerAndGetNext("Aldo");
		log.debug("testNestedInvocation() answered first question");

		assertNotNull(future);

		// step 2

		res = future.get();
		log.debug("testNestedInvocation() got response: " + res);
		assertFalse(res.isResult());
		assertEquals("lastname", res.getMessage());

		future = res.answerAndGetNext("Bucchi");

		// step 3

		res = future.get();
		log.debug("testNestedInvocation() got response: " + res);

		assertTrue(res.isResult());
		assertEquals("ALDO BUCCHI", res.getValue());
	}

}
