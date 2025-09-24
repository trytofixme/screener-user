package ru.screener.user.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.screener.user.entity.user.TelegramUser;

import java.util.Optional;
import java.util.UUID;

public interface AppUserRepository extends JpaRepository<TelegramUser, UUID> {

    Optional<TelegramUser> findByTgUserId(Long tgUserId);
}
