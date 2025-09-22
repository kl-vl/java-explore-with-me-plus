package ru.practicum.mainservice.event.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EventFilterBase {
    /** public + admin
     * список идентификаторов категорий в которых будет вестись поиск
     */
    private List<Long> categories;

    // public + admin
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rangeStart;

    // public + admin
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rangeEnd;

    // public + admin
    @PositiveOrZero
    @Builder.Default
    private int from = 0;

    // public + admin
    @Min(value = 1)
    @Max(value = 1000)
    @Builder.Default
    private int size = 10;
}
