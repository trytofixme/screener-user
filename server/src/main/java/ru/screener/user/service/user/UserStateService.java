package ru.screener.user.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.screener.user.entity.user.State;
import ru.screener.user.entity.user.UserState;
import ru.screener.user.entity.user.AppUser;
import ru.screener.user.repository.user.UserStateRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserStateService {

    private final UserStateRepository repository;

    @Transactional
    public void setState(AppUser user, State state) {
        UserState userState = repository.findByUser(user).orElseGet(() -> {
            UserState newState = new UserState();
            newState.setUser(user);
            newState.setState(State.NONE);
            return newState;
        });

        userState.setState(state);
        UserState savedState = repository.save(userState);
        log.info("{}", savedState);
    }

    @Transactional(readOnly = true)
    public State getState(AppUser user) {
        return repository.findByUser(user)
                .map(UserState::getState)
                .orElse(State.NONE);
    }
}
