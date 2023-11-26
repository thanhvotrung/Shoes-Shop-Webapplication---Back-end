package com.ecom.Controller.admin;

import com.ecom.Entity.Category;
import com.ecom.Model.mapper.CategoryMapper;
import com.ecom.Model.request.CreateCategoryRequest;
import com.ecom.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/api/admin/categories")
    public ResponseEntity<Object> adminGetListCategories(@RequestParam(defaultValue = "", required = false) String id,
                                                         @RequestParam(defaultValue = "", required = false) String name,
                                                         @RequestParam(defaultValue = "", required = false) String status,
                                                         @RequestParam(defaultValue = "0", required = false) Integer page) {
        Page<Category> categories = categoryService.adminGetListCategory(id, name, status, page);
        return ResponseEntity.ok(categories);
    };

    @GetMapping("/api/admin/categories-status")
    public ResponseEntity<Object> adminGetListCategoriesStatus() {
        List<Category> categories = categoryService.adminGetListCategoryWhereStatus("1");
        return ResponseEntity.ok(categories);
    };

    @GetMapping("/api/admin/categories/{id}")
    public ResponseEntity<Object> getCategoryById(@PathVariable long id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(CategoryMapper.toCategoryDTO(category));
    };

    @PostMapping("/api/admin/categories")
    public ResponseEntity<Object> createCategory(@Valid @RequestBody CreateCategoryRequest createCategoryRequest) {
        Category category = categoryService.createCategory(createCategoryRequest);
        return ResponseEntity.ok(CategoryMapper.toCategoryDTO(category));
    }

    @PutMapping("/api/admin/categories/{id}")
    public ResponseEntity<Object> updateCategory(@Valid @RequestBody CreateCategoryRequest createCategoryRequest, @PathVariable long id) {
        categoryService.updateCategory(createCategoryRequest, id);
        return ResponseEntity.ok("Sửa danh mục thành công!");
    }

    @DeleteMapping("/api/admin/categories/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Xóa danh mục thành công!");
    }

    @PutMapping("/api/admin/categories")
    public ResponseEntity<Object> updateOrderCategory(@RequestBody int[] ids){
        categoryService.updateOrderCategory(ids);
        return ResponseEntity.ok("Thay đổi thứ tự thành công!");
    }

}
