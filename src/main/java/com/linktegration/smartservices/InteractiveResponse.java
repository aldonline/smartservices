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

	InteractiveResponse(InteractiveRequest<T> request, boolean result,
			Object message, T value) {
		this.request = request;
		this.result = result;
		this.message = message;
		this.value = value;
		this.nextFuture = new SimpleFuture<InteractiveResponse<T>>();
	}

	/**
	 * InteractiveResponses can be either a question or a result.
	 * The result marks the end of your request, but a question must be answered
	 * in order to proceed.
	 * 
	 * One InteractiveRequest can result in many InteractiveResponses.
	 * 
	 * Check this method to see whether you are dealing with the result or 
	 * an intermediate question.
	 * 
	 * @return
	 */
	public boolean isResult() {
		return result;
	}

	/**
	 * If this.isResult() == false, then we are a question.
	 * The message is the info that the service is sending you.
	 * Once you are ready to answer, call {@link #answerAndGetNext(Object)}).
	 * 
	 * @return
	 */
	public Object getMessage() {
		return message;
	}

	/**
	 * If isResult() == true, then this is the result value.
	 * @return
	 */
	public T getValue() {
		return value;
	}

	/**
	 * Called by the InteractiveRequest that owns us
	 * @param res
	 */
	void resolveNext(InteractiveResponse<T> res) {
		this.nextFuture.resolve(res);
	}

	/**
	 * Once you are ready to answer an InteractiveResponse
	 * ( that was not a result ), just pass the object to this
	 * method. You will get a Future<InteractiveResponse> to the 
	 * next reponse in the chain.
	 * 
	 * @param o
	 * @return
	 * @throws RequestAlreadyFinishedException
	 */
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