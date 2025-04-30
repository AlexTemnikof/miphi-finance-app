package ru.rfma.statistics.services;

import org.springframework.stereotype.Service;
import ru.rfma.core.entities.Category;
import ru.rfma.core.entities.Operation;
import ru.rfma.core.enums.OperationType;
import ru.rfma.core.repo.CategoryRepository;
import ru.rfma.core.repo.OperationRepository;

import java.util.*;
@Service
public class StatisticServiceImpl {

    final private CategoryRepository categoryRepository;
    final private OperationRepository operationRepository;

    public StatisticServiceImpl(final CategoryRepository categoryRepository, final OperationRepository operationRepository) {
        this.categoryRepository = categoryRepository;
        this.operationRepository = operationRepository;
    }

    public Map<Category, Float> getExpensesPerCategory() {
        Map<Category, Float> map = new HashMap<>();
        List<Category> categories = categoryRepository.findAll();
        for (Category category : categories) {
            List<Operation> operations = operationRepository.getOperationsByCategoryIdAndOperationType(category.getId(), OperationType.Expense);
            for (Operation operation : operations) {
                updateValueCategoryMap(map, categoryRepository.getCategoryById(operation.getCategoryId()), operation.getAmount());
            }
        }
        return map;
    }

    public Map<Category, Float> getIncomesPerCategory() {
        Map<Category, Float> map = new HashMap<>();
        List<Category> categories = categoryRepository.findAll();
        for (Category category : categories) {
            List<Operation> operations = operationRepository.getOperationsByCategoryIdAndOperationType(category.getId(), OperationType.Income);
            for (Operation operation : operations) {
                updateValueCategoryMap(map, categoryRepository.getCategoryById(operation.getCategoryId()), operation.getAmount());
            }
        }
        return map;
    }

    public Map<Integer, Float> getExpensesPerMonth() {
        Map<Integer, Float> map = new HashMap<>();
        List<Operation> operations = operationRepository.getOperationsByOperationType(OperationType.Expense);
        for (Operation operation : operations) {
            updateValueDateMap(map, operation.getDate().getMonth(), operation.getAmount());
        }
        return map;
    }

    public Map<Integer, Float> getProfitPerMonth() {
        Map<Integer, Float> map = new HashMap<>();
        List<Operation> operations = operationRepository.findAll();
        for (Operation operation : operations) {
            if (map.containsKey(operation.getDate().getMonth())) {
                if (operation.getOperationType() == OperationType.Income) {
                    map.put(operation.getDate().getMonth(), map.get(operation.getDate().getMonth()) + operation.getAmount());
                } else {
                    map.put(operation.getDate().getMonth(), map.get(operation.getDate().getMonth()) - operation.getAmount());
                }
            } else {
                map.put(operation.getDate().getMonth(), operation.getAmount());
            }
        }
        return map;

    }
    private static void updateValueCategoryMap(Map<Category, Float> map, Category key, Float value) {
        if (map.containsKey(key)) {
            map.put(key, map.get(key) + value);
        } else {
            map.put(key, value);
        }
    }

    private static void updateValueDateMap(Map<Integer, Float> map, int key, Float value) {
        if (map.containsKey(key)) {
            map.put(key, map.get(key) + value);
        } else {
            map.put(key, value);
        }
    }

}
