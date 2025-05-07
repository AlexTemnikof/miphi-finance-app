package ru.rfma.core.services;

import org.springframework.stereotype.Service;
import ru.rfma.core.dto.CategoryDto;
import ru.rfma.core.dto.OperationDto;
import ru.rfma.core.entities.Category;
import ru.rfma.core.entities.Operation;
import ru.rfma.core.enums.OperationStatus;
import ru.rfma.core.mapper.CategoryMapper;
import ru.rfma.core.mapper.OperationMapper;
import ru.rfma.core.repo.CategoryRepository;
import ru.rfma.core.repo.OperationRepository;
import ru.rfma.core.util.Util;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CoreServiceImpl {
    private final CategoryRepository categoryRepository;

    private final OperationRepository operationRepository;
    private final OperationMapper operationMapper;
    private final CategoryMapper categoryMapper;

    private static List<OperationStatus>

    public CoreServiceImpl(final CategoryRepository categoryRepository,
                           final OperationRepository operationRepository,
                           final OperationMapper operationMapper,
                           final CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.operationRepository = operationRepository;
        this.operationMapper = operationMapper;
        this.categoryMapper = categoryMapper;
    }

    public CategoryDto createCategory(final String name, final Float spendLimit, final int userId) {
        Category category = new Category(name, spendLimit, userId);
        categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }

    public List<CategoryDto> getAllCategories(final Integer userId) {
        final List<Category> categories = categoryRepository.findCategoriesByUserId(userId);
        return categoryMapper.toDtos(categories);
    }

    public CategoryDto getCategoryById(final Integer id, final Integer userId) {
        return categoryMapper.toDto(categoryRepository.getCategoryByUserIdAndId(userId, id));
    }

    public CategoryDto getCategoryByName(final String name, final Integer userId) {
        return categoryMapper.toDto(categoryRepository.getCategoryByUserIdAndName(userId, name));
    }

    public CategoryDto updateCategoryLimit(final Integer id, final Float spendLimit) {
         final Category category = categoryRepository.getById(id);
         category.setSpendLimit(spendLimit);
         categoryRepository.save(category);
         return categoryMapper.toDto(category);
    }

    public void deleteCategory(final Integer id) {
         categoryRepository.deleteById(id);
    }

    public OperationDto createOperation(final OperationDto operationDto) {
        operationDto.setDate(new Date());
        Operation operation = operationMapper.toEntity(operationDto);
        final var savedOperation = operationRepository.save(operation);
        return operationMapper.toDto(savedOperation);
    }

    public OperationDto getOperationById(final Integer id, final Integer userId) {
        Operation operation = operationRepository.getOperationByUserIdAndId(userId, id);
        return operationMapper.toDto(operation);
    }

    public List<OperationDto> getAllOperations(final Integer userId) {
        return operationMapper.toDtos(operationRepository.findOperationsByUserId(userId));
    }

    public void deleteOperationById(final Integer id, final Integer userId) {
        final Optional<Operation> savedOperationOptional = this.operationRepository.findById(id);
        if (savedOperationOptional.isEmpty()) {
            throw new RuntimeException("The operation cannot be found");
        }
        final Operation savedOperation = savedOperationOptional.get();
        if (!savedOperation.getUserId().equals(userId)) {
            throw new UnsupportedOperationException("Cannot mark not yours operation as deleted");
        }

        if (!savedOperation.getStatus().isCanDelete()) {
            throw new UnsupportedOperationException("Cannot mark as deleted operation with such status");
        }

        savedOperation.setStatus(OperationStatus.CANCELLED);
        operationRepository.save(savedOperation);
    }

    public OperationDto updateOperation(final OperationDto operationDto, final Integer userId) {
        if (operationDto.getId() == null) {
            throw new IllegalArgumentException("The id must not be null");
        }

        final Operation updatedOperation = operationMapper.toEntity(operationDto);

        final Optional<Operation> savedOperationOptional = this.operationRepository.findById(operationDto.getId());

        if (savedOperationOptional.isEmpty()) {
            final Operation savedUpdatedOperation = this.operationRepository.save(updatedOperation);
            return operationMapper.toDto(savedUpdatedOperation);
        }

        // если нашлась операция по айди из дто
        final Operation savedOperation = savedOperationOptional.get();
        if (!savedOperation.getUserId().equals(userId)) {
            throw new UnsupportedOperationException("You cannot update not yours transactions");
        }

        if (!savedOperation.getStatus().isCanModify()) {
            throw new UnsupportedOperationException("The status of operation doesn't allow to edit this");
        }

        final List<String> updatedFields = Util.getFieldDifferences(updatedOperation, savedOperation);
        if (updatedFields.contains("senderAccountId") || updatedFields.contains("receiverAccountId") || updatedFields.contains("operationType")) {
            throw new UnsupportedOperationException("You cannot update operation status");
        }

        final Operation savedUpdatedOperation = this.operationRepository.save(updatedOperation);
        return operationMapper.toDto(savedUpdatedOperation);
    }
}
