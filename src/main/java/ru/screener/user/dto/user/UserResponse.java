package ru.screener.user.dto.user;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Locale;

@Data
@Accessors(chain = true)
public class UserResponse {

    private String telegramId;
    private String username;
    private String phone;
    private Locale locale;
}
