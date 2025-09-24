package ru.screener.user.dto.telegram.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class TelegramUpdate implements Serializable {

    @JsonProperty("update_id")
    private Long updateId;
    private Message message;
    @JsonProperty("callback_query")
    private CallbackQuery callbackQuery;
}
