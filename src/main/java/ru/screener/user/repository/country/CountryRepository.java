package ru.screener.user.repository.country;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.screener.user.entity.country.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
}