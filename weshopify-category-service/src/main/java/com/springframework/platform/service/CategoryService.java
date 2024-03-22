package com.springframework.platform.service;

import com.springframework.platform.bean.CategoryBean;

import java.util.List;

public interface CategoryService {

    CategoryBean createCategory(CategoryBean categoryBean);

    CategoryBean updateCategory(CategoryBean categoryBean);
    List<CategoryBean> findAllCategories();

    CategoryBean findCategoryById(int catId);

    List<CategoryBean> deleteCategory(int catId);

    List<CategoryBean> findAllChilds(int parentId);
}
