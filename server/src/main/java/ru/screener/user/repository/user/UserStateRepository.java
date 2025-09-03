package ru.screener.user.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.screener.user.entity.user.AppUser;
import ru.screener.user.entity.state.UserState;

import java.util.Optional;
import java.util.UUID;

public interface UserStateRepository extends JpaRepository<UserState, UUID> {

    Optional<UserState> findByUser(AppUser user);
}
