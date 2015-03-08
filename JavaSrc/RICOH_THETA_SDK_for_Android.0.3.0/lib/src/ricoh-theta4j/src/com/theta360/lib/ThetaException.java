package com.theta360.lib;

public class ThetaException extends Exception {

	private static final long serialVersionUID = 1L;

	private int status;

	public int getStatus() {
		return this.status;
	}

	public ThetaException(Throwable cause) {
		super(cause);
	}

	public ThetaException(String message) {
		super(message);
	}

	public ThetaException(String message, Throwable cause) {
		super(message, cause);
	}

	public ThetaException(String message, int status) {
		super(message);
		this.status = status;
	}
}
