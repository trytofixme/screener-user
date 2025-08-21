package ru.screener.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.screener.user.entity.AppUser;
import ru.screener.user.entity.UserState;

import java.util.Optional;
import java.util.UUID;

public interface UserStateRepository extends JpaRepository<UserState, UUID> {

    Optional<UserState> findByUser(AppUser user);
}
