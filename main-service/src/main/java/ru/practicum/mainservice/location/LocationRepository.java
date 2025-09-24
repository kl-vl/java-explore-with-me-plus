package ru.practicum.mainservice.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByLatAndLon(BigDecimal lat, BigDecimal lon);
}
