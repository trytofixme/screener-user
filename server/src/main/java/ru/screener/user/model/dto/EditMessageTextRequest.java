package ru.screener.user.model.dto;

public record EditMessageTextRequest(Long chat_id,
                                     Integer message_id,
                                     String text,
                                     Object reply_markup,
                                     String parse_mode)
{}
