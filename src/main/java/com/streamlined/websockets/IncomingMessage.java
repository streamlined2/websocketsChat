package com.streamlined.websockets;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncomingMessage {
	
	private String author;
	
	private LocalDateTime timeSent;
	
	private String topic;

	private String message;

}
