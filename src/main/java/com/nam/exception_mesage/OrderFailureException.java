package com.nam.exception_mesage;

public class OrderFailureException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public OrderFailureException(String messageError) {
		super(messageError);
	}

	public OrderFailureException(String messageError, Throwable throwable) {
		super(messageError, throwable);
	}

	public OrderFailureException() {
		super();
	}

}
