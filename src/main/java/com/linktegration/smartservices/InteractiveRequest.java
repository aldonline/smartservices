package com.linktegration.smartservices;

import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.linktegration.smartservices.util.RequestNotFinishedException;
import com.linktegration.smartservices.util.SimpleFuture;

public class InteractiveRequest<T> implements RequestInfo<T> {

	private Log log = LogFactory.getLog(InteractiveRequest.class);		
	
	// we wrap a SimpleRequest ( composition pattern )
	private SimpleRequest<T> request;

	SimpleFuture<InteractiveResponse<T>> firstFuture;
	InteractiveResponse<T> currentResponse;

	private Object answer = null;
	private boolean answered = false;

	InteractiveRequest() {
		// we create the first future, which will be returned
		// when the user calls getFirst()
		this.firstFuture = new SimpleFuture<InteractiveResponse<T>>();
		// we don't initialize yet. we wait till references are done
	}

	private void dispatchResponse(InteractiveResponse<T> res) {
		if (this.firstFuture.isDone() == false) {
			this.firstFuture.resolve(res);
		} else {
			this.currentResponse.resolveNext(res);
		}
		this.currentResponse = res;
	}

	void setup(SimpleRequest<T> request) {
		this.request = request;
		// create a reference we can access from within another thread
		final InteractiveRequest<T> self = this;
		new Thread(new Runnable() {
			public void run() {
				try {
					// we wait for the service to finish
					T value = self.request.getTargetService().getFuture().get();
					// and we create the final InteractiveResponse
					InteractiveResponse<T> res = new InteractiveResponse<T>(
							self, true, null, value);
					self.dispatchResponse(res);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public Future<InteractiveResponse<T>> getFirst() {
		return this.firstFuture;
	}

	/**
	 * Called by the client
	 * 
	 */
	public void answer(Object o) {
		log.debug("answer( " + o + " )");
		this.answer = o;
		this.answered = true;
	}

	/**
	 * Called by a service
	 */
	public Object ask(Object message) {
		// we are in the service that is asking
		log.debug("ask( " + message + " )");
		InteractiveResponse<T> res = new InteractiveResponse<T>(this, false,
				message, null);
		// reset the state so we can wait for an answer
		this.answer = null;
		this.answered = false;
		// and now we resume the client thread
		log.debug("ask() we will now send a response to the client with the message so it can answer. in the meantime we wait");
		this.dispatchResponse(res);
		while (this.answered == false) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		log.debug("ask() ok, the client has answered. we return this to the service");
		Object a = this.answer;
		this.answer = null;
		return a;
	}

	public Service<T> getTargetService() {
		return request.getTargetService();
	}

	public long getStartTime() {
		return request.getStartTime();
	}

	public long getEndTime() throws RequestNotFinishedException {
		return request.getEndTime();
	}

	public long getElapsedTime() {
		return request.getElapsedTime();
	}

	public boolean cancel(boolean mayInterruptIfRunning) {
		return request.cancel(mayInterruptIfRunning);
	}

	public boolean isCancelled() {
		return request.isCancelled();
	}

	public boolean isDone() {
		return request.isDone();
	}

}
