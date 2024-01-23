package com.streamlined.websockets.client;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import com.streamlined.websockets.OutgoingMessage;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class SessionHandler implements StompSessionHandler {

	@Override
	public Type getPayloadType(StompHeaders headers) {
		return OutgoingMessage.class;
	}

	@Override
	public void handleFrame(StompHeaders headers, Object payload) {
		if (payload instanceof OutgoingMessage message) {
			log.info("Message [{}] handled ({})", message, LocalDateTime.now());
		}
	}

	@Override
	public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
		log.info("Connected to session {} ({})", session, LocalDateTime.now());
	}

	@Override
	public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload,
			Throwable exception) {
		log.info("Exception arose: session {}, command {}, exception {} ({})", session, command, exception,
				LocalDateTime.now());
	}

	@Override
	public void handleTransportError(StompSession session, Throwable exception) {
		log.info("Transport error occurred: session {}, exception {} ({})", session, exception, LocalDateTime.now());
	}

}
