package com.nam.exception_mesage;

public class ValidFormException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ValidFormException(String messageError, Throwable throwable) {
		super(messageError, throwable);
	}

	public ValidFormException(String messageError) {
		super(messageError);
	}

	public ValidFormException() {
		super();
	}
}
