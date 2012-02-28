package com.linktegration.smartservices;

import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.linktegration.smartservices.util.RequestAlreadyFinishedException;
import com.linktegration.smartservices.util.SimpleFuture;

public class InteractiveResponse<T> {

	private Log log = LogFactory.getLog(InteractiveResponse.class);

	private InteractiveRequest<T> request;
	private boolean result;
	private Object message;
	private T value;
	private SimpleFuture<InteractiveResponse<T>> nextFuture;

	public InteractiveResponse(InteractiveRequest<T> request, boolean result,
			Object message, T value) {
		this.request = request;
		this.result = result;
		this.message = message;
		this.value = value;
		this.nextFuture = new SimpleFuture<InteractiveResponse<T>>();
	}

	public boolean isResult() {
		return result;
	}

	public Object getMessage() {
		return message;
	}

	public T getValue() {
		return value;
	}

	void resolveNext(InteractiveResponse<T> res) {
		this.nextFuture.resolve(res);
	}

	public Future<InteractiveResponse<T>> answerAndGetNext(Object o)
			throws RequestAlreadyFinishedException {
		log.debug("answerAndGetNext(" + o + ")");
		if (this.isResult()) { // you can not answer a final response
			throw new RequestAlreadyFinishedException();
		}
		// we answer on the request
		// this should unblock the service thread
		// and replace the previous future with a new one
		// which we return right away
		// the Request will take care of resolving it
		this.request.answer(o);
		return this.nextFuture;
	}

	public String toString() {
		if (isResult())
			return "InteractiveResponse(result=" + this.value + ")";
		else
			return "InteractiveReponse(message=" + this.message + ")";
	}

}