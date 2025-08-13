package ru.screener.user.exception.country;

import jakarta.persistence.EntityNotFoundException;

public class CountryNotFoundException extends EntityNotFoundException {
    public CountryNotFoundException(long countryId) {
        super(String.format("Country with id %d not found", countryId));
    }
}
