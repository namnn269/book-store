package com.nam.exception_mesage;

public class ObjectNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ObjectNotFoundException(String messageError) {
		super(messageError);
	}

	public ObjectNotFoundException(String messageError, Throwable throwable) {
		super(messageError, throwable);
	}

	public ObjectNotFoundException() {
		super();
	}

}
