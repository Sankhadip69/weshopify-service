package com.springframework.platform.resource;

import com.springframework.platform.bean.CategoryBean;
import com.springframework.platform.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class CategoryResource {


    private CategoryService categoryService;

    public CategoryResource(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "findAllCategories", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryBean>> findAllCategories() {
        return ResponseEntity.ok( categoryService.findAllCategories());
    }

    @Operation(summary = "findChildCategories", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/categories/childs/{parentId}")
    public ResponseEntity<List<CategoryBean>> findChildCategories(@PathVariable("parentId") int parentId) {
        return ResponseEntity.ok( categoryService.findAllChilds(parentId));
    }

    @Operation(summary = "createCategory", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/categories")
    public ResponseEntity<CategoryBean> createCategory(@RequestBody CategoryBean categoryBean) {
        return ResponseEntity.ok(categoryService.createCategory(categoryBean));
    }

    @Operation(summary = "updateCategory", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/categories")
    public ResponseEntity<CategoryBean> updateCategory(@RequestBody CategoryBean categoryBean) {
        return ResponseEntity.ok(categoryService.updateCategory(categoryBean));
    }

    @Operation(summary = "deleteCategory", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/categories/{catId}")
    public ResponseEntity<List<CategoryBean>> deleteCategory(@PathVariable("catId") int catId) {
        return ResponseEntity.ok(categoryService.deleteCategory(catId));
    }

    @Operation(summary = "getCategoryById", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/categories/{catId}")
    public ResponseEntity<CategoryBean> getCategoryById(@PathVariable("catId") int catId) {
        return ResponseEntity.ok(categoryService.findCategoryById(catId));
    }
}
