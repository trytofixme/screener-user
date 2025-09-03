package ru.screener.user.repository.settings;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.screener.user.entity.user.AppUser;
import ru.screener.user.entity.settings.RsiSettings;

import java.util.Optional;
import java.util.UUID;

public interface RsiSettingsRepository extends JpaRepository<RsiSettings, UUID> {

    Optional<RsiSettings> findByUser(AppUser user);
}
