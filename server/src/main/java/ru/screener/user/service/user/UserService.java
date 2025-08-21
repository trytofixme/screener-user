package ru.screener.user.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.screener.user.entity.AppUser;
import ru.screener.user.entity.ExchangePref;
import ru.screener.user.entity.UserSettings;
import ru.screener.user.entity.UserState;
import ru.screener.user.repository.AppUserRepository;
import ru.screener.user.repository.ExchangePrefRepository;
import ru.screener.user.repository.UserSettingsRepository;
import ru.screener.user.repository.UserStateRepository;

import java.math.BigDecimal;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final AppUserRepository userRepo;
    private final UserSettingsRepository settingsRepo;
    private final ExchangePrefRepository exchangeRepo;
    private final UserStateRepository stateRepo;

    @Transactional
    public AppUser upsertFromTelegram(Long tgUserId, Long chatId, String username, String first, String last) {
        var user = userRepo.findByTgUserId(tgUserId).orElseGet(() -> {
            var u = new AppUser();
            u.setTgUserId(tgUserId);
            u.setChatId(chatId);
            u.setCreatedAt(Instant.now());
            return u;
        });

        user.setUsername(username);
        user.setFirstName(first);
        user.setLastName(last);
        user.setChatId(chatId);
        user.setUpdatedAt(Instant.now());
        user = userRepo.save(user);

        AppUser finalUser = user;
        settingsRepo.findByUser(user).orElseGet(() -> {
            var s = new UserSettings();
            s.setUser(finalUser);
            s.setLongPeriodMin(20); s.setShortPeriodMin(15); s.setDumpPeriodMin(16);
            s.setLongPercent(new BigDecimal("7.0"));
            s.setShortPercent(new BigDecimal("10.0"));
            s.setDumpPercent(new BigDecimal("-10.0"));
            s.setUpdatedAt(Instant.now());
            return settingsRepo.save(s);
        });

        exchangeRepo.findByUser(user).orElseGet(() -> {
            var e = new ExchangePref();
            e.setUser(finalUser);
            e.setBybitEnabled(true);
            e.setMoexEnabled(false);
            e.setUpdatedAt(Instant.now());
            return exchangeRepo.save(e);
        });

        stateRepo.findByUser(user).orElseGet(() -> {
            var s = new UserState();
            s.setUser(finalUser);
            s.setState(UserState.State.NONE);
            s.setUpdatedAt(Instant.now());
            return stateRepo.save(s);
        });

        return user;
    }

    @Transactional(readOnly = true)
    public UserSettings getSettings(AppUser u) { return settingsRepo.findByUser(u).orElseThrow(); }

    @Transactional
    public void updatePeriods(AppUser u, Integer longMin, Integer shortMin, Integer dumpMin) {
        var s = settingsRepo.findByUser(u).orElseThrow();
        if (longMin != null) s.setLongPeriodMin(longMin);
        if (shortMin != null) s.setShortPeriodMin(shortMin);
        if (dumpMin != null) s.setDumpPeriodMin(dumpMin);
        s.setUpdatedAt(Instant.now());
    }

    @Transactional
    public void updatePercents(AppUser u, BigDecimal longP, BigDecimal shortP, BigDecimal dumpP) {
        var s = settingsRepo.findByUser(u).orElseThrow();
        if (longP != null) s.setLongPercent(longP);
        if (shortP != null) s.setShortPercent(shortP);
        if (dumpP != null) s.setDumpPercent(dumpP);
        s.setUpdatedAt(Instant.now());
    }

    @Transactional
    public ExchangePref toggleBybit(AppUser u) {
        var e = exchangeRepo.findByUser(u).orElseThrow();
        e.setBybitEnabled(!e.isBybitEnabled());
        e.setUpdatedAt(Instant.now());
        return e;
    }

    @Transactional
    public ExchangePref toggleMoex(AppUser u) {
        var e = exchangeRepo.findByUser(u).orElseThrow();
        e.setMoexEnabled(!e.isMoexEnabled());
        e.setUpdatedAt(Instant.now());
        return e;
    }

    @Transactional
    public void setState(AppUser u, UserState.State state) {
        var s = stateRepo.findByUser(u).orElseThrow();
        s.setState(state);
        s.setUpdatedAt(Instant.now());
    }

    @Transactional(readOnly = true)
    public UserState.State getState(AppUser u) {
        return stateRepo.findByUser(u).map(UserState::getState).orElse(UserState.State.NONE);
    }

    @Transactional(readOnly = true)
    public ExchangePref getExchanges(AppUser u) {
        return exchangeRepo.findByUser(u).orElseThrow();
    }
}
