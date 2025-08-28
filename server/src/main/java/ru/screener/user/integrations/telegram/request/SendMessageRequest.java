package ru.screener.user.integrations.telegram.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendMessageRequest implements Serializable {

    @JsonProperty("chat_id")
    public Long chatId;

    @JsonProperty("text")
    private String text;

    @JsonProperty("reply_markup")
    private Map<String, Object> replyMarkup;

    @JsonProperty("parse_mode")
    private String parseMode = "Markdown";

    @JsonProperty("disable_web_page_preview")
    private Boolean disableWebPagePreview = true;
}