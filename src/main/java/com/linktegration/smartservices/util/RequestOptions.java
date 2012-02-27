package com.linktegration.smartservices.util;

public class RequestOptions {
	private int timeout;
	  // sets the priority of this request
	  // the priority will then be propagated to the underlying threads
	  // note that since many requests may be consuming the same underlying operation
	  // we need to find a heuristic to calculate the priority of a thread at a given point in time
	  // for example, we could say that highest priority takes precedence
	private int priority;
	private boolean acceptInteraction;
	  // if the consumer operation is cached
	  // and the producer operation is invalidated
	  // then we will also be invalidated
	  // this is only relevant if the operation is cached
	private boolean acceptInvalidation;
	
	
	public static RequestOptions getDefault(){
		return new RequestOptions();
	}
}
