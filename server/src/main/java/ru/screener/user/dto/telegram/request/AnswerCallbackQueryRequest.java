package ru.screener.user.dto.telegram.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class AnswerCallbackQueryRequest implements Serializable {
    @JsonProperty("chat_id")
    public Integer chatId;

    public String title;

    public String description;

    public String payload;

    public String currency;

    public List<LabeledPrice> prices;

    @JsonProperty("provider_token")
    public String providerToken;

    @Data
    @Accessors(chain = true)
    public static class LabeledPrice implements Serializable {
        public String label;
        public Integer amount;
    }
}
