package ru.screener.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.screener.user.entity.AppUser;
import ru.screener.user.entity.UserSettings;

import java.util.Optional;
import java.util.UUID;

public interface UserSettingsRepository extends JpaRepository<UserSettings, UUID> {

    Optional<UserSettings> findByUser(AppUser user);
}
