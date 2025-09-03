package ru.screener.user.error.kafka;

public class InvalidKafkaMessageException extends RuntimeException {

    public InvalidKafkaMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
