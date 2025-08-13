package ru.screener.user.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.screener.user.dto.user.UserRegistrationRequest;
import ru.screener.user.dto.user.UserRegistrationResponse;
import ru.screener.user.dto.user.UserResponse;
import ru.screener.user.service.user.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String telegramId) {
        log.debug("User controller accepted request get user with id {}", telegramId);

        UserResponse response = userService.getUserByTelegramId(telegramId);
        log.debug("User controller return response get user {}", response);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/registration")
    public ResponseEntity<UserRegistrationResponse> registrationUser
            (@RequestBody @Valid UserRegistrationRequest userRegisterRequestDto) {
        log.debug("User controller accepted request registration user {}", userRegisterRequestDto);

        UserRegistrationResponse response = userService.registrationUser(userRegisterRequestDto);
        log.debug("User controller return response registration user {}", response);
        return ResponseEntity.ok(response);
    }
}
