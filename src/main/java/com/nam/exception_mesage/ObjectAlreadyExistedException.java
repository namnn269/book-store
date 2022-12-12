package com.nam.exception_mesage;

public class ObjectAlreadyExistedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ObjectAlreadyExistedException(String messageError) {
		super(messageError);
	}

	public ObjectAlreadyExistedException(String messageError, Throwable throwable) {
		super(messageError, throwable);
	}

	public ObjectAlreadyExistedException() {
		super();
	}

}
