package com.mybank.exception;

public class AccountOperationException extends RuntimeException {
	private static final long serialVersionUID = 4684483370796712863L;

	public AccountOperationException(String message) {
		super(message);
	}
}
