package ru.screener.user.service.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.screener.user.dto.user.UserRegistrationRequest;
import ru.screener.user.dto.user.UserRegistrationResponse;
import ru.screener.user.dto.user.UserResponse;
import ru.screener.user.entity.country.Country;
import ru.screener.user.entity.subscription.Subscription;
import ru.screener.user.entity.user.User;
import ru.screener.user.exception.user.UserNotFoundException;
import ru.screener.user.mapper.user.UserEntityMapper;
import ru.screener.user.repository.user.UserRepository;
import ru.screener.user.service.country.CountryService;
import ru.screener.user.service.subscription.SubscriptionService;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserEntityMapper userMapper;
    private final CountryService countryService;
    private final SubscriptionService premiumService;

    @Transactional(readOnly = true)
    public UserResponse getUserByTelegramId(String telegramId) {
        User user = userRepository.findById(telegramId)
                .orElseThrow(() -> {
                    log.error("User with telegramId {} not found", telegramId);
                    return new UserNotFoundException(telegramId);
                });

        return userMapper.toUserResponse(user);
    }

    @Transactional
    public UserRegistrationResponse registrationUser(@Valid UserRegistrationRequest userRegisterRequestDto) {
        User user = new User();
        user.setTelegramId(userRegisterRequestDto.getTelegramId());
        user.setUsername(userRegisterRequestDto.getUsername());
        user.setPhone(userRegisterRequestDto.getPhone());
        user.setAboutMe(userRegisterRequestDto.getAboutMe());

        long countryId = user.getCountry().getId();
        Country country = countryService.getCountryById(countryId);
        user.setCountry(country);

        user.setCity(userRegisterRequestDto.getCity());

        Subscription premium = premiumService.getSubscriptionByTelegramId(user.getTelegramId());
        user.setSubscription(premium);

        userRepository.save(user);

        return new UserRegistrationResponse().setUsername(user.getUsername());
    }
}
