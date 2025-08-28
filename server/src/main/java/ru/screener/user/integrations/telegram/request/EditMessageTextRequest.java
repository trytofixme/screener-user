package ru.screener.user.integrations.telegram.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EditMessageTextRequest implements Serializable {

    @JsonProperty("chat_id")
    public Long chatId;

    @JsonProperty("message_id")
    public Integer messageId;

    @JsonProperty("text")
    private String text;

    @JsonProperty("reply_markup")
    private Object replyMarkup;

    @JsonProperty("parse_mode")
    private String parseMode = "Markdown";
}
