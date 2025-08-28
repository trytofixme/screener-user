package ru.screener.user.dto.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Message implements Serializable {

    @JsonProperty("message_id")
    private Long messageId;
    private From from;
    private Chat chat;
    private String text;
}
