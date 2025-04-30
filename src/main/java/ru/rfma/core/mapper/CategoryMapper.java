package ru.rfma.core.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.rfma.core.dto.CategoryDto;
import ru.rfma.core.entities.Category;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface CategoryMapper {
    CategoryDto toDto(Category category);
    List<CategoryDto> toDtos(List<Category> categoryList);

    Category toEntity(CategoryDto categoryDto);
    List<Category> toEntities(List<CategoryDto> categoryDtoList);
}