package ru.screener.user.dto.telegram.request;

public record AnswerCallbackQueryRequest(String callback_query_id,
                                         String text,
                                         Boolean show_alert)
{}
