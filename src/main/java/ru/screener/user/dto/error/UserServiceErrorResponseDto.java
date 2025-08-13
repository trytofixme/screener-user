package ru.screener.user.dto.error;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class UserServiceErrorResponseDto {

    private String errorMsg;
    private LocalDateTime timestamp;
    private int codeResponse;
}
