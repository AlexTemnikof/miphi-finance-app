package ru.rfma.core.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.rfma.core.dto.CategoryDto;
import ru.rfma.core.entities.Category;
import ru.rfma.core.mapper.CategoryMapper;
import ru.rfma.core.mapper.OperationMapper;
import ru.rfma.core.repo.CategoryRepository;
import ru.rfma.core.repo.OperationRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class CoreServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private OperationRepository operationRepository;

    @Mock
    private OperationMapper operationMapper;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CoreServiceImpl coreService;

    private Category category;
    private CategoryDto categoryDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        category = new Category("Food", 100.0f, 1); // Предполагается, что userId=1
        categoryDto = new CategoryDto();
        categoryDto.setId(100500);
        categoryDto.setName("Food");
        categoryDto.setSpendLimit(100.0f);
        categoryDto.setUserId(2);
    }

    @Test
    public void testCreateCategory() {
        when(categoryMapper.toDto(any(Category.class))).thenReturn(categoryDto);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryDto createdCategory = coreService.createCategory("Food", 100.0f, 1);

        assertNotNull(createdCategory);
        assertEquals("Food", createdCategory.getName());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    public void testGetAllCategories() {
        when(categoryRepository.findCategoriesByUserId(1)).thenReturn(Arrays.asList(category));
        when(categoryMapper.toDtos(anyList())).thenReturn(Arrays.asList(categoryDto));

        List<CategoryDto> categories = coreService.getAllCategories(1);

        assertNotNull(categories);
        assertEquals(1, categories.size());
        assertEquals("Food", categories.get(0).getName());
        verify(categoryRepository).findCategoriesByUserId(1);
    }

    @Test
    public void testGetCategoryById() {
        when(categoryRepository.getCategoryByUserIdAndId(1, 1)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto foundCategory = coreService.getCategoryById(1, 1);

        assertNotNull(foundCategory);
        assertEquals("Food", foundCategory.getName());
        verify(categoryRepository).getCategoryByUserIdAndId(1, 1);
    }



    @Test
    public void testGetCategoryByName() {
        when(categoryRepository.getCategoryByUserIdAndName(1, "Food")).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto foundCategory = coreService.getCategoryByName("Food", 1);

        assertNotNull(foundCategory);
        assertEquals("Food", foundCategory.getName());
        verify(categoryRepository).getCategoryByUserIdAndName(1, "Food");
    }

}
