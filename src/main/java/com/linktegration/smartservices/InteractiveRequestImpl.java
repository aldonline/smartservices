package com.linktegration.smartservices;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.linktegration.smartservices.util.InteractiveResponse;
import com.linktegration.smartservices.util.RequestNotFinishedException;

public class InteractiveRequestImpl<T> implements InteractiveRequest<T> {

	// we wrap a SimpleRequest ( composition pattern )
	private SimpleRequest<T> request;
	
	private Future<InteractiveResponse<T>> future;
	
	public InteractiveRequestImpl(SimpleRequest<T> request) {
		this.request = request;
		request.attachInteractiveRequest(this);
	}
	
	
	public Future<InteractiveResponse<T>> getNext(){
		return null;
	}
	
	/**
	 * Called by the client
	 * 
	 */
	public void answer( Object o ) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Called by a service
	 */
	public Object ask( Object message ){
		InteractiveResponse<T> res = new InteractiveResponse<T>( false, message, null );
		return null;
	}
	
	
	
	
	
	public InteractiveResponse<T> get() throws InterruptedException, ExecutionException {
		throw new RuntimeException("You cannot get() on an interactive request. Use getNext() instead");
	}

	public InteractiveResponse<T> get(long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		throw new RuntimeException("You cannot get() on an interactive request. Use getNext() instead");
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
