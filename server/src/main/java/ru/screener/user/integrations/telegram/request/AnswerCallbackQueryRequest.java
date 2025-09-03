package ru.screener.user.integrations.telegram.request;

public record AnswerCallbackQueryRequest(String callback_query_id,
                                         String text,
                                         Boolean show_alert)
{}
