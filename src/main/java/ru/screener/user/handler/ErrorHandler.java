package ru.screener.user.handler;

@FunctionalInterface
public interface ErrorHandler {
    String handle(Exception ex);
}
