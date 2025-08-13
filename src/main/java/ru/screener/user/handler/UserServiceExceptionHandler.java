package ru.screener.user.handler;

import feign.FeignException;
import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientException;
import ru.screener.user.dto.error.UserServiceErrorResponseDto;
import ru.screener.user.exception.authorization.UserUnauthorizedException;
import ru.screener.user.exception.country.CountryNotFoundException;
import ru.screener.user.exception.user.UserAlreadyExistsException;
import ru.screener.user.exception.user.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class UserServiceExceptionHandler {
    private static final Map<Class<? extends Exception>, HttpStatus> HTTP_STATUS_MAP = new HashMap<>();

    static {
        HTTP_STATUS_MAP.put(UserUnauthorizedException.class, HttpStatus.UNAUTHORIZED);
        HTTP_STATUS_MAP.put(UserNotFoundException.class, HttpStatus.NOT_FOUND);
        HTTP_STATUS_MAP.put(CountryNotFoundException.class, HttpStatus.NOT_FOUND);
        HTTP_STATUS_MAP.put(UserAlreadyExistsException.class, HttpStatus.CONFLICT);
        HTTP_STATUS_MAP.put(MethodArgumentNotValidException.class, HttpStatus.BAD_REQUEST);
        HTTP_STATUS_MAP.put(FeignException.class, HttpStatus.BAD_GATEWAY);
        HTTP_STATUS_MAP.put(RetryableException.class, HttpStatus.BAD_GATEWAY);
        HTTP_STATUS_MAP.put(WebClientException.class, HttpStatus.BAD_GATEWAY);
    }

    private static final Map<Class<? extends Exception>, ErrorHandler> errorHandlers = Map.of(
            MethodArgumentNotValidException.class, ex ->
                    formatMethodArgumentNotValidException((MethodArgumentNotValidException) ex),
            UserAlreadyExistsException.class, ex ->
                    formatUserAlreadyExistsException((UserAlreadyExistsException) ex)
    );

    @ExceptionHandler({
            UserUnauthorizedException.class,
            UserNotFoundException.class,
            CountryNotFoundException.class,
            UserAlreadyExistsException.class,
            MethodArgumentNotValidException.class,
            FeignException.class,
            RetryableException.class,
            WebClientException.class
    })
    public ResponseEntity<UserServiceErrorResponseDto> handleException(Exception ex) {
        ErrorHandler handler = getErrorHandler(ex);
        String errorMessage = handler.handle(ex);
        HttpStatus status = getHttpStatus(ex);

        return createErrorResponse(errorMessage, status, ex);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<UserServiceErrorResponseDto> handleGenericException(Exception ex) {
        log.error("Unhandled exception caught", ex);
        return createErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    private HttpStatus getHttpStatus(Throwable ex) {
        return HTTP_STATUS_MAP.getOrDefault(ex.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorHandler getErrorHandler(Throwable ex) {
        return errorHandlers.getOrDefault(ex.getClass(), Throwable::getMessage);
    }

    private ResponseEntity<UserServiceErrorResponseDto> createErrorResponse(String errorMsg,
                                                                            HttpStatus status,
                                                                            Exception ex) {
        log.error("Error in user-service: {}, response status {}", errorMsg, status, ex);
        UserServiceErrorResponseDto response =
                new UserServiceErrorResponseDto()
                        .setErrorMsg(errorMsg)
                        .setTimestamp(LocalDateTime.now())
                        .setCodeResponse(status.value());
        return new ResponseEntity<>(response, status);
    }

    private static String formatMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getAllErrors().stream()
                .map(error -> String.format("Field '%s' %s",
                        ((FieldError) error).getField(), error.getDefaultMessage()))
                .collect(Collectors.joining(", "));
    }

    private static String formatUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return String.format("Field '%s' with value %s already exists",
                ex.getField().name().toLowerCase(), ex.getValue());
    }
}
