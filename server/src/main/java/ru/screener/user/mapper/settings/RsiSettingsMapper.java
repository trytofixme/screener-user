package ru.screener.user.mapper.settings;

import org.springframework.stereotype.Component;
import ru.screener.user.dto.settings.RsiSettingsDto;
import ru.screener.user.entity.settings.RsiSettings;

@Component
public class RsiSettingsMapper {

    public RsiSettingsDto toDto(RsiSettings rsiSettings) {
        return new RsiSettingsDto()
                .setTelegramId(rsiSettings.getUser().getTgUserId())
                .setShortTimeFrame(rsiSettings.getShortTimeFrame() != null ? rsiSettings.getShortTimeFrame().getTranslation() : null)
                .setLongTimeFrame(rsiSettings.getLongTimeFrame() != null ? rsiSettings.getLongTimeFrame().getTranslation() : null)
                .setShortRsi(rsiSettings.getShortRsi())
                .setLongRsi(rsiSettings.getLongRsi())
                .setShortDump(rsiSettings.getShortDump())
                .setLongDump(rsiSettings.getLongDump());
    }
}
