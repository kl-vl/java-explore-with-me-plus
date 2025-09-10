package ru.practicum.statsserver;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StatsServerRepository extends JpaRepository<EndpointHitEntity, Long>, EndpointHitCustomRepository {

}
