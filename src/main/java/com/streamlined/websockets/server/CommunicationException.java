package com.streamlined.websockets.server;

public class CommunicationException extends RuntimeException {

	public CommunicationException(String message, Throwable cause) {
		super(message, cause);
	}

	public CommunicationException(String message) {
		super(message);
	}

}
