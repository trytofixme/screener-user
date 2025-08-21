package ru.screener.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.screener.user.entity.AppUser;
import ru.screener.user.entity.ExchangePref;

import java.util.Optional;
import java.util.UUID;

public interface ExchangePrefRepository extends JpaRepository<ExchangePref, UUID> {

    Optional<ExchangePref> findByUser(AppUser user);
}
