package ru.rfma.core.services;

import org.springframework.stereotype.Service;
import ru.rfma.core.dto.CategoryDto;
import ru.rfma.core.entities.Category;
import ru.rfma.core.entities.Operation;
import ru.rfma.core.dto.OperationDto;
import ru.rfma.core.mapper.CategoryMapper;
import ru.rfma.core.mapper.OperationMapper;
import ru.rfma.core.repo.CategoryRepository;
import ru.rfma.core.repo.OperationRepository;

import java.util.List;

@Service
public class CoreServiceImpl {
    private final CategoryRepository categoryRepository;

    private final OperationRepository operationRepository;
    private final OperationMapper operationMapper;
    private final CategoryMapper categoryMapper;

    public CoreServiceImpl(final CategoryRepository categoryRepository,
                           final OperationRepository operationRepository,
                           final OperationMapper operationMapper,
                           final CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.operationRepository = operationRepository;
        this.operationMapper = operationMapper;
        this.categoryMapper = categoryMapper;
    }

    public CategoryDto createCategory(final String name, final Float limit, final int userId) {
        Category category = new Category(name, limit, userId);
        categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }

    public List<CategoryDto> getAllCategories() {
        System.out.println("wtf");
        return categoryMapper.toDtos(categoryRepository.findAll());
    }

    public CategoryDto getCategoryById(final Integer id) {
        return categoryMapper.toDto(categoryRepository.getById(id));
    }

    public CategoryDto getCategoryByName(final String name) {
        return categoryMapper.toDto(categoryRepository.getCategoryByName(name));
    }

    public CategoryDto updateCategoryLimit(final Integer id, final Float limit) {
         final Category category = categoryRepository.getById(id);
         category.setSpendLimit(limit);
         categoryRepository.save(category);
         return categoryMapper.toDto(category);
    }

    public void deleteCategory(final Integer id) {
         categoryRepository.deleteById(id);
    }

    public OperationDto createOperation(final OperationDto operationDto) {
        Operation operation = operationMapper.toEntity(operationDto);
        operationRepository.save(operation);
        return operationMapper.toDto(operation);
    }

    public OperationDto getOperationById(final Integer id) {
        Operation operation = operationRepository.getById(id);
        return operationMapper.toDto(operation);
    }

    public List<OperationDto> getAllOperations() {
        return operationMapper.toDtos(operationRepository.findAll());
    }

    public void deleteOperationById(final Integer id) {
        operationRepository.deleteById(id);
    }
}
