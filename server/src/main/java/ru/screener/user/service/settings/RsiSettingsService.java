package ru.screener.user.service.settings;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.screener.user.dto.settings.RsiSettingsDto;
import ru.screener.user.entity.settings.RsiSettings;
import ru.screener.user.entity.settings.TimeFrame;
import ru.screener.user.entity.user.AppUser;
import ru.screener.user.error.settings.RsiSettingsNotFoundException;
import ru.screener.user.mapper.settings.RsiSettingsMapper;
import ru.screener.user.publisher.settings.RsiSettingsPublisher;
import ru.screener.user.repository.settings.RsiSettingsRepository;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class RsiSettingsService {

    private static final BigDecimal RSI_MIN = BigDecimal.valueOf(0.0);
    private static final BigDecimal RSI_MAX = BigDecimal.valueOf(100.0);

    private final RsiSettingsMapper settingsMapper;
    private final RsiSettingsPublisher rsiSettingsPublisher;
    private final RsiSettingsRepository repository;

    @Transactional
    public RsiSettings getOrCreateSettings(AppUser user) {
        return repository.findByUser(user).orElseGet(() -> createDefaultSettings(user));
    }

    @Transactional
    public RsiSettings createDefaultSettings(AppUser user) {
        RsiSettings settings = new RsiSettings();
        settings.setUser(user);
        // Default values:
        settings.setShortTimeFrame(TimeFrame.FIFTEEN_MINUTES);
        settings.setLongTimeFrame(TimeFrame.FIFTEEN_MINUTES);
        settings.setShortRsi(BigDecimal.valueOf(80));
        settings.setLongRsi(BigDecimal.valueOf(20));

        RsiSettings saved = repository.save(settings);
        publishAfterCommit(saved);
        return saved;
    }

    @Transactional(readOnly = true)
    public Flux<RsiSettingsDto> getRsiSettings() {
        return Mono.fromCallable(repository::findAll)
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapIterable(list -> list)
                .map(settingsMapper::toDto);
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
        publishAfterCommit(updatedSettings);
    }

    @Transactional
    public void updateShortRsi(AppUser user, BigDecimal shortRsi) {
        validateRsi(shortRsi);

        RsiSettings settings = repository.findByUser(user)
                .orElseThrow(RsiSettingsNotFoundException::new);

        settings.setShortRsi(shortRsi);

        RsiSettings updatedSettings = repository.save(settings);
        log.info("{}", updatedSettings);
        publishAfterCommit(updatedSettings);
    }

    @Transactional
    public void updateLongRsi(AppUser user, BigDecimal longRsi) {
        validateRsi(longRsi);

        RsiSettings settings = repository.findByUser(user)
                .orElseThrow(RsiSettingsNotFoundException::new);

        settings.setLongRsi(longRsi);

        RsiSettings updatedSettings = repository.save(settings);
        log.info("{}", updatedSettings);
        publishAfterCommit(updatedSettings);
    }

    private void validateRsi(BigDecimal rsiValue) {
        if (rsiValue == null || rsiValue.compareTo(RSI_MIN) < 0 || rsiValue.compareTo(RSI_MAX) > 0) {
            throw new IllegalArgumentException("Значение RSI может быть от 0 до 100");
        }
    }

    private void publishAfterCommit(RsiSettings settings) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override public void afterCommit() {
                    rsiSettingsPublisher.sendMessage(settingsMapper.toDto(settings));
                }
            });
        } else {
            rsiSettingsPublisher.sendMessage(settingsMapper.toDto(settings));
        }
    }
}
