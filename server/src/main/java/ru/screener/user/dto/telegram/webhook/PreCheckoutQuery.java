package ru.screener.user.dto.telegram.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PreCheckoutQuery {
    private String id;

    private From from;

    private String currency;

    @JsonProperty(value = "total_amount")
    private Integer totalAmount;

    @JsonProperty(value = "invoice_payload")
    private String invoicePayload;
}
