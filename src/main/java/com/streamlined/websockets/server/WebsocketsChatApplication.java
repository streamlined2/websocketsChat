package com.streamlined.websockets.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@SpringBootApplication
@EnableWebSocketMessageBroker
public class WebsocketsChatApplication implements WebSocketMessageBrokerConfigurer {

	public static final String SERVER_HOST = "ws://localhost:8080/";
	public static final String SERVER_ENDPOINT = "/websocket-server";
	public static final String APP_PREFIX = "/chat";
	public static final String RESPONSE_DESTINATION = "/topic/messages";
	public static final String REQUEST_ENDPOINT = "/process-message";
	private static final String TOPIC_NAME = "/topic";

	public static void main(String[] args) {
		SpringApplication.run(WebsocketsChatApplication.class, args);
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker(TOPIC_NAME);
		registry.setApplicationDestinationPrefixes(APP_PREFIX);
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint(SERVER_ENDPOINT);
	}

}
