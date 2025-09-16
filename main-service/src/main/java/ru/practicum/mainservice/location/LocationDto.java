package ru.practicum.mainservice.location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    private Long id;
    private BigDecimal lat;
    private BigDecimal lon;
}
