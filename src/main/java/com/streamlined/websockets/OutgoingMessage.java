package com.streamlined.websockets;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.NonNull;
import lombok.With;

@Builder
public record OutgoingMessage(@With @NonNull String author, @With @NonNull LocalDateTime timeSent,
		@With @NonNull LocalDateTime timeReplied, String topic, @With @NonNull String message) {
}
