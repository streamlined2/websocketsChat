package com.streamlined.websockets;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.NonNull;
import lombok.With;

@Builder
public record IncomingMessage(@With @NonNull String author, @With @NonNull LocalDateTime timeSent,
		@With @NonNull String topic, @With @NonNull String message) {
}
