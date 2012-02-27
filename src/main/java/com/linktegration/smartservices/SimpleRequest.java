package com.linktegration.smartservices;

import java.util.concurrent.Future;

public interface SimpleRequest<T> extends Future<T>, Request<T> {

	public Service<?> getConsumer();

	void attachInteractiveRequest(InteractiveRequest<T> req);

	InteractiveRequest<T> getAttachedInteractiveRequest();

}
