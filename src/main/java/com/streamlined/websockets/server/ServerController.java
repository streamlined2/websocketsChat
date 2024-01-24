package com.streamlined.websockets.server;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.streamlined.websockets.IncomingMessage;
import com.streamlined.websockets.OutgoingMessage;

import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
public class ServerController {

	private static final long RESPONSE_DELAY_INTERVAL = 1L;

	@MessageMapping(WebsocketsChatApplication.REQUEST_ENDPOINT)
	@SendTo(WebsocketsChatApplication.RESPONSE_DESTINATION)
	public OutgoingMessage handle(IncomingMessage message) {
		try {
			return processMessage(message);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			log.error("handling interrupted: {}", e);
			throw new CommunicationException("handling interrupted", e);
		}
	}

	private OutgoingMessage processMessage(IncomingMessage message) throws InterruptedException {
		Thread.sleep(Duration.of(RESPONSE_DELAY_INTERVAL, ChronoUnit.SECONDS));
		return new OutgoingMessage("Response from server:[%s]".formatted(message.getMessage()));
	}

}
