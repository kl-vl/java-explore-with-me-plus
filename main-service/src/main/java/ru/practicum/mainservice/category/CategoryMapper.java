package ru.practicum.mainservice.category;

import org.mapstruct.Mapper;
import ru.practicum.mainservice.config.CommonMapperConfiguration;

@Mapper(config = CommonMapperConfiguration.class)
public interface CategoryMapper {

    //@Mapping(target = "id", ignore = true)
    Category toEntity(CategoryDto categoryDto);

    CategoryDto toDto(Category entity);
}
