package ru.screener.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.screener.user.entity.AppUser;

import java.util.Optional;
import java.util.UUID;

public interface AppUserRepository extends JpaRepository<AppUser, UUID> {

    Optional<AppUser> findByTgUserId(Long tgUserId);
}
