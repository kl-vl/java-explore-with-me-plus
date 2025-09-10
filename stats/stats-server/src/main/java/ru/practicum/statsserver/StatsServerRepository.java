package ru.practicum.statsserver;

import org.springframework.data.jpa.repository.JpaRepository;


public interface StatsServerRepository extends JpaRepository<EndpointHitEntity, Long>, EndpointHitCustomRepository {

}
