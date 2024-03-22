package com.springframework.platform.repository;

import com.springframework.platform.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query(name = "findChildsOfAParent", value = "from Category cat where cat.parent.id=:patId")
    List<Category> findChildsOfAParent(@Param("patId") int parentId);
}
