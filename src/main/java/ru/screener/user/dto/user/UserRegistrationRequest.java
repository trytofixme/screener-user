package ru.screener.user.dto.user;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserRegistrationRequest {

    private String telegramId;
    private String username;
    private String email;
    private String phone;
    private String aboutMe;
    private Long countryId;
    private String city;
}
