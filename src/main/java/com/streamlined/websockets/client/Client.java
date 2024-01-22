package com.streamlined.websockets.client;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import com.streamlined.websockets.IncomingMessage;
import com.streamlined.websockets.server.CommunicationException;
import com.streamlined.websockets.server.WebsocketsChatApplication;

public class Client {

	private static final long MESSAGE_DISPATCH_INTERVAL = 2L;
	private static final int COUNT = 10;

	public static void main(String... args) {

		WebSocketStompClient client = new WebSocketStompClient(new StandardWebSocketClient());
		client.setMessageConverter(new MappingJackson2MessageConverter());
		SessionHandler handler = new SessionHandler();

		CompletableFuture<StompSession> response = client.connectAsync(
				WebsocketsChatApplication.SERVER_HOST + WebsocketsChatApplication.SERVER_ENDPOINT, handler);

		try {
			var session = response.get();
			session.subscribe(WebsocketsChatApplication.RESPONSE_DESTINATION, handler);

			for (int k = 0; k < COUNT; k++) {
				session.send(
						"%s%s".formatted(WebsocketsChatApplication.APP_PREFIX,
								WebsocketsChatApplication.REQUEST_ENDPOINT),
						new IncomingMessage("Incoming message %d (%s)".formatted(k,
								DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()))));
				Thread.sleep(Duration.of(MESSAGE_DISPATCH_INTERVAL, ChronoUnit.SECONDS));
			}
			session.disconnect();
			client.stop();
		} catch (InterruptedException | ExecutionException e) {
			Thread.currentThread().interrupt();
			throw new CommunicationException("cannot obtain stomp session", e);
		}

	}

}
