package ru.screener.user.dto.telegram.keyboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class InlineKeyboardDto implements Serializable {
    @JsonProperty("inline_keyboard")
    private List<List<KeyboardButton>> keyboard;

    @Data
    @Accessors(chain = true)
    public static class KeyboardButton implements Serializable {
        private String text;

        private String url;
    }
}
