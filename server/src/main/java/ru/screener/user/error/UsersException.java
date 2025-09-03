package ru.screener.user.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class UsersException extends RuntimeException {

    private final String code;
    private final ErrorType errorType;
}
