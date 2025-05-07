package ru.rfma.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rfma.core.entities.Category;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> findCategoriesByUserId(Integer userId);
    Category getCategoryByUserIdAndId(Integer userId, int categoryId);

    Category getCategoryByUserIdAndName(Integer userId, String name);
}
