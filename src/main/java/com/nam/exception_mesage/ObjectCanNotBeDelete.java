package com.nam.exception_mesage;

public class ObjectCanNotBeDelete extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ObjectCanNotBeDelete(String messageError) {
		super(messageError);
	}

	public ObjectCanNotBeDelete(String messageError, Throwable throwable) {
		super(messageError, throwable);
	}

	public ObjectCanNotBeDelete() {
		super();
	}

}
