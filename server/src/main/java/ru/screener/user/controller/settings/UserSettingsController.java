package ru.screener.user.controller.settings;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.screener.user.dto.settings.RsiSettingsDto;
import ru.screener.user.service.settings.RsiSettingsService;

@Slf4j
@RestController
@RequestMapping("/api/v1/settings")
@RequiredArgsConstructor
public class UserSettingsController {

    private final RsiSettingsService rsiSettingsService;

    @GetMapping(value = "/rsi", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<RsiSettingsDto> getRsiSettings() {
        log.info("UserSettingsController.getRsiSettings()");

        return rsiSettingsService.getRsiSettings();
    }
}
