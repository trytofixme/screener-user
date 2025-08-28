package ru.screener.user.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.screener.user.entity.user.AppUser;

import java.util.Optional;
import java.util.UUID;

public interface AppUserRepository extends JpaRepository<AppUser, UUID> {

    Optional<AppUser> findByTgUserId(Long tgUserId);
}
