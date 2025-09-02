package ru.screener.user.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.screener.user.entity.user.AppUser;
import ru.screener.user.repository.user.AppUserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final AppUserRepository repository;

    @Transactional
    public AppUser upsertFromTelegram(Long tgUserId, Long chatId, String username, String firstName, String lastName) {
        return repository.findByTgUserId(tgUserId)
                .map(user -> updateUserFields(user, chatId, username, firstName, lastName))
                .map(repository::save)
                .orElseGet(() -> createUser(tgUserId, chatId, username, firstName, lastName));
    }

    private AppUser createUser(Long tgUserId, Long chatId, String username, String firstName, String lastName) {
        try {
            AppUser user = new AppUser();
            user.setTgUserId(tgUserId);
            user.setChatId(chatId);
            user.setUsername(username);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            return repository.save(user);
        } catch (DataIntegrityViolationException dup) {
            AppUser existing = repository.findByTgUserId(tgUserId).orElseThrow();
            updateUserFields(existing, chatId, username, firstName, lastName);
            return repository.save(existing);
        }
    }

    private static AppUser updateUserFields(AppUser user, Long chatId, String username, String firstName, String lastName) {
        user.setChatId(chatId);
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return user;
    }
}
