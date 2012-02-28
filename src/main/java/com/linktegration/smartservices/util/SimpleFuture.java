package com.linktegration.smartservices.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SimpleFuture<V> implements Future<V> {

	private V value;
	private boolean done = false;
	
	public void resolve( V value ){
		this.value = value;
		this.done = true;
	}
	
	public boolean cancel(boolean mayInterruptIfRunning) {
		// TODO Auto-generated method stub
		return false;
	}

	public V get() throws InterruptedException, ExecutionException {
		while ( this.done == false ){
			Thread.sleep(100);
		}
		return this.value;
	}

	// TODO: add timeout support
	public V get(long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		return this.get(); // we don't support timeout, just proxy to get()
	}

	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isDone() {
		return this.done;
	}

}
