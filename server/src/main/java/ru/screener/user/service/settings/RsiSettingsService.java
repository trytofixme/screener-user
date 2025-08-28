package ru.screener.user.service.settings;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.screener.user.entity.settings.TimeFrame;
import ru.screener.user.entity.user.AppUser;
import ru.screener.user.entity.settings.RsiSettings;
import ru.screener.user.error.settings.RsiSettingsNotFoundException;
import ru.screener.user.repository.settings.RsiSettingsRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class RsiSettingsService {

    private final RsiSettingsRepository repository;

    @Transactional
    public RsiSettings getSettingsByUser(AppUser user) {
        return repository.findByUser(user).orElseGet(() -> {
                RsiSettings newSettings = new RsiSettings();
                newSettings.setUser(user);
                return repository.save(newSettings);
            });
    }

    @Transactional
    public void updateTimeFrames(AppUser user, TimeFrame shortMin, TimeFrame longMin) {
        RsiSettings settings = repository.findByUser(user)
                .orElseThrow(RsiSettingsNotFoundException::new);

        if (shortMin != null) {
            settings.setShortTimeFrame(shortMin);
        }

        if (longMin != null) {
            settings.setLongTimeFrame(longMin);
        }

        RsiSettings updatedSettings = repository.save(settings);
        log.info("{}", updatedSettings);
    }

    @Transactional
    public void updateRsiValues(AppUser user, Integer shortRsi, Integer longRsi) {
        RsiSettings settings = repository.findByUser(user)
                .orElseThrow(RsiSettingsNotFoundException::new);

        if (shortRsi != null) {
            settings.setShortRsi(shortRsi);
        }

        if (longRsi != null) {
            settings.setLongRsi(longRsi);
        }

        RsiSettings updatedSettings = repository.save(settings);
        log.info("{}", updatedSettings);
    }
}
