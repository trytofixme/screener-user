package ru.screener.user.dto.telegram.keyboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class KeyboardDto implements Serializable {
    private List<List<KeyboardButton>> keyboard;

    @JsonProperty("resize_keyboard")
    private boolean resizeKeyboard;

    @JsonProperty("one_time_keyboard")
    private boolean oneTimeKeyboard;

    @Data
    @Accessors(chain = true)
    public static class KeyboardButton implements Serializable {
        private String text;
    }
}
