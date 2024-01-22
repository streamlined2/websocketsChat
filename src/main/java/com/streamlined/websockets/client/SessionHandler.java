package com.streamlined.websockets.client;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import com.streamlined.websockets.OutgoingMessage;

public class SessionHandler implements StompSessionHandler {

	@Override
	public Type getPayloadType(StompHeaders headers) {
		return OutgoingMessage.class;
	}

	@Override
	public void handleFrame(StompHeaders headers, Object payload) {
		OutgoingMessage message = (OutgoingMessage) payload;
		System.out.println("Message [%s] handled (%s)".formatted(message.toString(),
				DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now())));
	}

	@Override
	public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
		System.out.println("Connected to session %s (%s)".formatted(session.toString(),
				DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now())));
	}

	@Override
	public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload,
			Throwable exception) {
		System.out.println("Exception arose: session %s, command %s, exception %s (%s)".formatted(session.toString(),
				command.toString(), exception.toString(),
				DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now())));
	}

	@Override
	public void handleTransportError(StompSession session, Throwable exception) {
		System.out.println("Transport error occurred: session %s, exception %s (%s)".formatted(session.toString(),
				exception.toString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now())));
	}

}
