package com.linktegration.smartservices;

import java.util.concurrent.Future;

import com.linktegration.smartservices.util.InteractiveResponse;

public interface InteractiveRequest<T> extends Request<T>,
		Future<InteractiveResponse<T>> {
	
	public void answer(Object o);

	public Object ask(Object message);
	
	public Future<InteractiveResponse<T>> getNext();
	
}
