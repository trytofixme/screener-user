package ru.screener.user.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SendMessageRequest(
        @JsonProperty("chat_id") Long chatId,
        @JsonProperty("text") String text,
        @JsonProperty("reply_markup") Map<String, Object> replyMarkup,
        @JsonProperty("parse_mode") String parseMode,
        @JsonProperty("disable_web_page_preview") Boolean disableWebPagePreview
) {}