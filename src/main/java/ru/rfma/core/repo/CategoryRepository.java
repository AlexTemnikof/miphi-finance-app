package ru.rfma.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rfma.core.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category getCategoryById(int categoryId);

    Category getCategoryByName(String name);
}
