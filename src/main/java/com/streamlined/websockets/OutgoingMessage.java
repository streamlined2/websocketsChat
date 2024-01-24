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
public class OutgoingMessage {

	private String author;

	private LocalDateTime timeSent;

	private LocalDateTime timeReplied;

	private String topic;

	private String message;

}
