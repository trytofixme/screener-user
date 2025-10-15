package ru.screener.user.dto.telegram.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.screener.user.dto.telegram.keyboard.InlineKeyboardDto;
import ru.screener.user.dto.telegram.keyboard.KeyboardDto;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendMessageRequest implements Serializable {

    @JsonProperty("chat_id")
    public Long chatId;

    @JsonProperty("text")
    private String text;

    @JsonProperty("reply_markup")
    private Object replyMarkup;

    @JsonProperty("parse_mode")
    private String parseMode = "Markdown";

    @JsonProperty("disable_web_page_preview")
    private Boolean disableWebPagePreview = true;

    public SendMessageRequest setKeyboard(KeyboardDto keyboard) {
        this.replyMarkup = keyboard;
        return this;
    }

    public SendMessageRequest setInlineKeyboard(InlineKeyboardDto inlineKeyboard) {
        this.replyMarkup = inlineKeyboard;
        return this;
    }
}