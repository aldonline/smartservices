package com.linktegration.smartservices;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.linktegration.smartservices.util.RequestNotFinishedException;
import com.linktegration.smartservices.util.RequestOptions;
import com.linktegration.smartservices.util.ServiceNotStartedException;

public class SimpleRequest<T> implements RequestInfo<T>, Future<T> {

	private Service<?> consumer;
	private Service<T> targetService;
	private RequestOptions options;

	private long startTime;
	private long endTime;

	private Future<T> future;

	private InteractiveRequest<T> interactiveRequest = null;

	protected SimpleRequest(Service<?> consumer, Service<T> targetService,
			RequestOptions options) {
		this.consumer = consumer;
		this.targetService = targetService;
		this.options = options;
		start();
	}

	private void start() {
		// lets start running
		startTime = Calendar.getInstance().getTimeInMillis();
	}

	public boolean cancel(boolean mayInterruptIfRunning) {
		// TODO we don't cancel the underlying future
		// we cancel this request only
		return false;
	}

	private void grabFuture() {
		try {
			this.future = targetService.getFuture();
		} catch (ServiceNotStartedException e) {
			e.printStackTrace();
		}
	}

	public T get() throws InterruptedException, ExecutionException {
		// proxy to underlying future ( obtained from service )
		grabFuture();
		return future.get();
	}

	public T get(long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		grabFuture();
		return future.get(timeout, unit);
	}

	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isDone() {
		return future.isDone();
	}

	public Service<?> getConsumer() {
		return consumer;
	}

	public Service<T> getTargetService() {
		return targetService;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getEndTime() throws RequestNotFinishedException {
		return endTime;
	}

	public long getElapsedTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void attachInteractiveRequest(InteractiveRequest<T> req) {
		interactiveRequest = req;
	}

	public InteractiveRequest<T> getAttachedInteractiveRequest() {
		return interactiveRequest;
	}

}
