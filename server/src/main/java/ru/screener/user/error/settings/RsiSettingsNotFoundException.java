package ru.screener.user.error.settings;

import ru.screener.user.error.ErrorType;
import ru.screener.user.error.UsersException;

public class RsiSettingsNotFoundException extends UsersException {

    public RsiSettingsNotFoundException() {
        super("error.01", ErrorType.INTERNAL);
    }
}
