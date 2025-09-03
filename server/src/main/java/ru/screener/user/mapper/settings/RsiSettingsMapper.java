package ru.screener.user.mapper.settings;

import org.springframework.stereotype.Component;
import ru.screener.user.dto.settings.RsiSettingsDto;
import ru.screener.user.entity.settings.RsiSettings;

@Component
public class RsiSettingsMapper {

    public RsiSettingsDto toDto(RsiSettings s) {
        return new RsiSettingsDto()
                .setTelegramId(s.getUser().getTgUserId())
                .setShortTimeFrame(s.getShortTimeFrame() != null ? s.getShortTimeFrame().getTranslation() : null)
                .setLongTimeFrame(s.getLongTimeFrame() != null ? s.getLongTimeFrame().getTranslation() : null)
                .setShortRsi(s.getShortRsi())
                .setLongRsi(s.getLongRsi());
    }
}
