package com.middlegames.shkbot;

@SuppressWarnings("serial")
public class SHKConnectionErrorException extends SHKException {

	public SHKConnectionErrorException() {
		super();
	}

	public SHKConnectionErrorException(String message, Throwable cause) {
		super(message, cause);
	}

	public SHKConnectionErrorException(String message) {
		super(message);
	}

	public SHKConnectionErrorException(Throwable cause) {
		super(cause);
	}
}
