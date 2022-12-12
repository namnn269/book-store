package com.nam.exception_mesage;

public class OverQuantityException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public OverQuantityException(String messageError) {
		super(messageError);
	}

	public OverQuantityException(String messageError, Throwable throwable) {
		super(messageError, throwable);
	}

	public OverQuantityException() {
		super();
	}

}
