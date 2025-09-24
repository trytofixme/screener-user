package ru.screener.user.service.settings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.screener.user.dto.settings.RsiSettingsDto;
import ru.screener.user.entity.settings.RsiSettings;
import ru.screener.user.model.settings.TimeFrame;
import ru.screener.user.entity.user.TelegramUser;
import ru.screener.user.mapper.settings.RsiSettingsMapper;
import ru.screener.user.publisher.settings.RsiSettingsPublisher;
import ru.screener.user.repository.settings.RsiSettingsRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RsiSettingsServiceTest {

    @Mock
    private RsiSettingsMapper mapper;
    @Mock
    private RsiSettingsPublisher publisher;
    @Mock
    private RsiSettingsRepository repository;

    @InjectMocks
    private RsiSettingsService service;

    private TelegramUser user;
    private RsiSettings settings;

    @BeforeEach
    void setUp() {
        user = new TelegramUser();
        user.setTgUserId(1L);
        user.setChatId(2L);

        settings = new RsiSettings();
        settings.setUser(user);
    }

    @Test
    void getOrCreateSettingsReturnsExisting() {
        when(repository.findByUser(user)).thenReturn(Optional.of(settings));

        RsiSettings result = service.getOrCreateSettings(user);

        assertThat(result).isSameAs(settings);
        verify(repository, never()).save(any());
    }

    @Test
    void getOrCreateSettingsCreatesDefaultsWhenMissing() {
        when(repository.findByUser(user)).thenReturn(Optional.empty());
        when(repository.save(any(RsiSettings.class))).thenAnswer(invocation -> invocation.getArgument(0));
        RsiSettingsDto dto = new RsiSettingsDto();
        when(mapper.toDto(any())).thenReturn(dto);

        RsiSettings result = service.getOrCreateSettings(user);

        assertThat(result.getShortTimeFrame()).isEqualTo(TimeFrame.FIFTEEN_MINUTES);
        assertThat(result.getLongTimeFrame()).isEqualTo(TimeFrame.FIFTEEN_MINUTES);
        assertThat(result.getShortRsi()).isEqualTo(BigDecimal.valueOf(80));
        assertThat(result.getLongRsi()).isEqualTo(BigDecimal.valueOf(20));
        verify(repository).save(any(RsiSettings.class));
        verify(publisher).sendMessage(dto);
    }

    @Test
    void updateShortRsiSavesAndPublishes() {
        when(repository.findByUser(user)).thenReturn(Optional.of(settings));
        when(repository.save(settings)).thenReturn(settings);
        RsiSettingsDto dto = new RsiSettingsDto();
        when(mapper.toDto(settings)).thenReturn(dto);

        service.updateShortRsi(user, BigDecimal.valueOf(50));

        assertThat(settings.getShortRsi()).isEqualByComparingTo("50");
        verify(repository).save(settings);
        verify(publisher).sendMessage(dto);
    }

    @Test
    void updateTimeFramesUpdatesBothAndPublishes() {
        when(repository.findByUser(user)).thenReturn(Optional.of(settings));
        when(repository.save(settings)).thenReturn(settings);
        RsiSettingsDto dto = new RsiSettingsDto();
        when(mapper.toDto(settings)).thenReturn(dto);

        service.updateTimeFrames(user, TimeFrame.ONE_HOUR, TimeFrame.FOUR_HOURS);

        assertThat(settings.getShortTimeFrame()).isEqualTo(TimeFrame.ONE_HOUR);
        assertThat(settings.getLongTimeFrame()).isEqualTo(TimeFrame.FOUR_HOURS);
        verify(repository).save(settings);
        verify(publisher).sendMessage(dto);
    }

    @Test
    void updateShortRsiWithInvalidValueThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> service.updateShortRsi(user, BigDecimal.valueOf(150)));
        verify(repository, never()).findByUser(any());
    }
}
