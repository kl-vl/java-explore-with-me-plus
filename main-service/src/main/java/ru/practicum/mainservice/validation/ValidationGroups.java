package ru.practicum.mainservice.validation;

public interface ValidationGroups {
    interface Create {}

    interface Update {}

    interface CreateAndUpdate extends Create, Update {}
}
