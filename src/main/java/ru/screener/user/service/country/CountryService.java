package ru.screener.user.service.country;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.screener.user.entity.country.Country;
import ru.screener.user.exception.country.CountryNotFoundException;
import ru.screener.user.repository.country.CountryRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class CountryService {

    private final CountryRepository countryRepository;

    public Country getCountryById(long countryId) {
        return countryRepository.findById(countryId)
                .orElseThrow(() -> {
                    log.error("Country with id {} not found", countryId);
                    return new CountryNotFoundException(countryId);
                });
    }
}
