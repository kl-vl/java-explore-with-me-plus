package ru.practicum.stats.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.stats.model.EndpointHitEntity;
import ru.practicum.stats.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EndpointHitCustomRepositoryImpl implements EndpointHitCustomRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<ViewStatsDto> getViewStats(List<String> uris, LocalDateTime start, LocalDateTime end, boolean unique) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ViewStatsDto> query = cb.createQuery(ViewStatsDto.class);
        Root<EndpointHitEntity> root = query.from(EndpointHitEntity.class);

        query.select(cb.construct(ViewStatsDto.class,
                root.get("app"),
                root.get("uri"),
                unique ? cb.countDistinct(root.get("ip")) : cb.count(root.get("ip"))
        ));

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.between(root.get("timestamp"), start, end));

        if (uris != null && !uris.isEmpty()) {
            predicates.add(root.get("uri").in(uris));
        }
        query.where(predicates.toArray(new Predicate[0]));

        query.groupBy(root.get("app"), root.get("uri"));
        query.orderBy(cb.desc(unique ? cb.countDistinct(root.get("ip")) : cb.count(root.get("ip"))));

        return entityManager.createQuery(query).getResultList();
    }

}