package ru.screener.user.dto.telegram;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Chat implements Serializable {

    private Long id;
    private String type;
}
