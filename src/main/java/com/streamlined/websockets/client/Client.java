package com.streamlined.websockets.client;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.streamlined.websockets.IncomingMessage;
import com.streamlined.websockets.OutgoingMessage;
import com.streamlined.websockets.server.CommunicationException;
import com.streamlined.websockets.server.WebsocketsChatApplication;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class Client implements Runnable, StompSessionHandler {

	private static final long MESSAGE_DISPATCH_INTERVAL = 2L;
	private static final int ITERATION_COUNT = 10;

	private final int id;

	public Client(int id) {
		this.id = id;
	}

	@Override
	public void run() {

		WebSocketStompClient client = new WebSocketStompClient(new StandardWebSocketClient());
		var mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		var converter = new MappingJackson2MessageConverter();
		converter.setObjectMapper(mapper);
		client.setMessageConverter(converter);

		CompletableFuture<StompSession> response = client
				.connectAsync(WebsocketsChatApplication.SERVER_HOST + WebsocketsChatApplication.SERVER_ENDPOINT, this);

		try {
			var session = response.get();
			session.subscribe(WebsocketsChatApplication.RESPONSE_DESTINATION, this);

			for (int k = 0; k < ITERATION_COUNT; k++) {
				sendMessage(session, k);
				Thread.sleep(Duration.of(MESSAGE_DISPATCH_INTERVAL, ChronoUnit.SECONDS));
			}
			session.disconnect();
			client.stop();
		} catch (InterruptedException | ExecutionException e) {
			Thread.currentThread().interrupt();
			log.error("cannot obtain stomp session: {}", e);
			throw new CommunicationException("cannot obtain stomp session", e);
		}
	}

	private void sendMessage(StompSession session, int iteration) {
		var message = IncomingMessage.builder().author("Client #%d".formatted(id)).timeSent(LocalDateTime.now())
				.topic("Conversation").message("Message #%d".formatted(iteration)).build();
		session.send(getDestination(), message);
	}

	private String getDestination() {
		return "%s%s".formatted(WebsocketsChatApplication.APP_PREFIX, WebsocketsChatApplication.REQUEST_ENDPOINT);
	}

	@Override
	public Type getPayloadType(StompHeaders headers) {
		return OutgoingMessage.class;
	}

	@Override
	public void handleFrame(StompHeaders headers, Object payload) {
		if (payload instanceof OutgoingMessage message) {
			log.info("Client {} handled message [{}] ({})", id, message, LocalDateTime.now());
		}
	}

	@Override
	public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
		log.info("Client {} connected to session {} ({})", id, session, LocalDateTime.now());
	}

	@Override
	public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload,
			Throwable exception) {
		log.info("Exception arose in client {}: session {}, command {}, exception {} ({})", id, session, command,
				exception, LocalDateTime.now());
	}

	@Override
	public void handleTransportError(StompSession session, Throwable exception) {
		log.info("Transport error occurred in client {}: session {}, exception {} ({})", id, session, exception,
				LocalDateTime.now());
	}

}
