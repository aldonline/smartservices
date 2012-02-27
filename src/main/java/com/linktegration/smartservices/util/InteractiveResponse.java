package com.linktegration.smartservices.util;

public class InteractiveResponse<T> {

	private boolean result;
	private Object message;
	private T value;

	public InteractiveResponse(boolean result, Object message, T value) {
		this.result = result;
		this.message = message;
		this.value = value;
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
}