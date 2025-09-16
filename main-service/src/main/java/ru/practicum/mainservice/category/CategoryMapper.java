package ru.practicum.mainservice.category;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    //@Mapping(target = "id", ignore = true)
    Category toEntity(CategoryDto categoryDto);

    CategoryDto toDto(Category entity);
}
