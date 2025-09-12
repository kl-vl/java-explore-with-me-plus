package ru.practicum.stats.model;

import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = {"id"})
@Table(name = "endpoint_hits")
public class EndpointHitEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String app;

    @Column(nullable = false)
    private String uri;

    @Column(nullable = false)
    private String ip;

    @Column(name = "hit_timestamp", nullable = false)
    private LocalDateTime timestamp;
}
