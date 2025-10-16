package ru.screener.user.dto.telegram.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnswerPreCheckoutRequest {
    @JsonProperty(value = "pre_checkout_query_id")
    private String queryId;

    private Boolean ok;
}
