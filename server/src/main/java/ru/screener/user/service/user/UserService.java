package ru.screener.user.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.screener.user.entity.user.AppUser;
import ru.screener.user.repository.user.AppUserRepository;
import ru.screener.user.service.settings.RsiSettingsService;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final AppUserRepository repository;

    @Transactional
    public AppUser upsertFromTelegram(Long tgUserId, Long chatId, String username, String firstName, String lastName) {
        AppUser user = repository.findByTgUserId(tgUserId).orElseGet(() -> {
            AppUser newUser = new AppUser();
            newUser.setTgUserId(tgUserId);
            newUser.setChatId(chatId);
            return newUser;
        });

        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setChatId(chatId);
        user = repository.save(user);

        return user;
    }
}
