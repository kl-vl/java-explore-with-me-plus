package ru.practicum.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.stats.model.EndpointHitEntity;

@Repository
public interface StatsServerRepository extends JpaRepository<EndpointHitEntity, Long>, EndpointHitCustomRepository {

}
