package ru.screener.user.model.dto;

public record AnswerCallbackQueryRequest(String callback_query_id,
                                         String text,
                                         Boolean show_alert)
{}
