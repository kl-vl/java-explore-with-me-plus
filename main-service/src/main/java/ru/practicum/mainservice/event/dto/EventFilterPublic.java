package ru.practicum.mainservice.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.practicum.mainservice.event.enums.EventSort;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class EventFilterPublic extends EventFilterBase {

    /** Public
     *  Текст для поиска в содержимом аннотации и подробном описании события
     */
    private String text;

    // Public
    private Boolean paid;

    // Public
    private Boolean onlyAvailable;

    /** Public
     *  Вариант сортировки: по дате события или по количеству просмотров
     *  Available values : EVENT_DATE, VIEWS
     */
    private EventSort sort;


}
