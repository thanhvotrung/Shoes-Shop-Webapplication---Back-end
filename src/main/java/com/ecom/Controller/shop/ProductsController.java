package com.ecom.Controller.shop;

import com.ecom.Entity.Brand;
import com.ecom.Entity.Category;
import com.ecom.Model.dto.PageableDTO;
import com.ecom.Model.request.FilterProductRequest;
import com.ecom.Service.BrandService;
import com.ecom.Service.CategoryService;
import com.ecom.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@CrossOrigin("*")
@RestController
public class ProductsController {
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/api/client/check-quantity-product")
    public ResponseEntity<Integer> getQuantitySizeProduct(@RequestParam(required = true) String id, @RequestParam(required = true) String size){
            return ResponseEntity.ok(productService.checkQuantitySizeProduct(id, size));
    }

    @PostMapping("/api/client/products")
    public ResponseEntity<Object> getProductShopPages(@RequestBody(required = false) FilterProductRequest fil) {
        String name = "";
        String sortingOption = "";
        List<Long> brandIds = new ArrayList<>();
        List<Long> categoryIds = new ArrayList<>();
        List<Integer> sizes = new ArrayList<>();
        Long minPrice = (long) 0;
        Long maxPrice = Long.MAX_VALUE;
        int page = 1;
        if (Objects.isNull(fil)) {
            List<Brand> brands = brandService.getListBrand();
            for (Brand brand : brands) {
                brandIds.add(brand.getId());
            }
            List<Category> categories = categoryService.getListCategories();
            for (Category category : categories) {
                categoryIds.add(category.getId());
            }
        } else {
//            Lấy tên sản phẩm
            if (!fil.getName().equals("")) {
                name = fil.getName();
            }

            //Lấy danh sách nhãn hiệu
            if (fil.getBrands().size() == 0) {
                List<Brand> brands = brandService.getListBrand();
                for (Brand brand : brands) {
                    brandIds.add(brand.getId());
                }
            } else {
                for (Long brandId : fil.getBrands()) {
                    System.out.println(brandId);
                    brandIds.add(brandId);
                }
            }
            //Lấy danh sách danh mục
            if (fil.getCategories().size() == 0) {
                List<Category> categories = categoryService.getListCategories();
                for (Category category : categories) {
                    categoryIds.add(category.getId());
                }
            } else {
                for (Long categoryId : fil.getCategories()) {
                    categoryIds.add(categoryId);
                }
            }
            if (fil.getSizes().size() > 0) {
                for (Integer size : fil.getSizes()) {
                    sizes.add(size);
                }
            }
            page = fil.getPage();
            minPrice = fil.getMinPrice();
            maxPrice = fil.getMaxPrice();
            sortingOption = fil.getSortingOption();
        }
        // danh sách sản phẩm
        FilterProductRequest req = new FilterProductRequest(name, brandIds, categoryIds, sizes, minPrice, maxPrice, page, sortingOption);
        PageableDTO result = productService.filterProduct(req);

        return ResponseEntity.ok(result);
    }


    //    API lấy danh dách brands
    @GetMapping("/api/client/brands")
    public ResponseEntity<Object> getBrands() {
        List<Brand> brands = brandService.getListBrand();
        return ResponseEntity.ok(brands);
    }

    //    API lấy danh sách categories
    @GetMapping("/api/client/categories")
    public ResponseEntity<Object> getCategories() {
        List<Category> categories = categoryService.getListCategories();
        return ResponseEntity.ok(categories);
    }

    //    API tìm kiếm sản phẩm
    @GetMapping("/api/client/search")
    public ResponseEntity<Object> searchProduct(@RequestParam(required = false) String keyword, @RequestParam(required = false) Integer page) {
        PageableDTO result = productService.searchProductByKeyword(keyword, page);
        return ResponseEntity.ok(result);
    }
}
