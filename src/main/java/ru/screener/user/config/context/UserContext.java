package ru.screener.user.config.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.screener.user.exception.authorization.UserUnauthorizedException;

@Component
@Slf4j
public class UserContext {

    private final ThreadLocal<Long> userIdHolder = new ThreadLocal<>();

    public void setUserId(long userId) {
        userIdHolder.set(userId);
    }

    public long getUserId() {
        Long userId = userIdHolder.get();
        if (userId == null) {
            String errorMsg = "User ID is missing. Please make sure 'x-user-id' header is included in the request.";
            log.error(errorMsg);
            throw new UserUnauthorizedException(errorMsg);
        }
        return userId;
    }

    public void clear() {
        userIdHolder.remove();
    }
}
