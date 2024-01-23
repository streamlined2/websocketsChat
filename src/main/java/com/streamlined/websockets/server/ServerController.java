package com.streamlined.websockets.server;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.streamlined.websockets.IncomingMessage;
import com.streamlined.websockets.OutgoingMessage;

@Controller
public class ServerController {

	private static final long RESPONSE_DELAY_INTERVAL = 1L;

	@MessageMapping(WebsocketsChatApplication.REQUEST_ENDPOINT)
	@SendTo(WebsocketsChatApplication.RESPONSE_DESTINATION)
	public OutgoingMessage handle(IncomingMessage message) {
		try {
			Thread.sleep(Duration.of(RESPONSE_DELAY_INTERVAL, ChronoUnit.SECONDS));
			return new OutgoingMessage("Response from server:[" + message.getMessage() + "]");
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new CommunicationException("handling interrupted", e);
		}
	}

}
