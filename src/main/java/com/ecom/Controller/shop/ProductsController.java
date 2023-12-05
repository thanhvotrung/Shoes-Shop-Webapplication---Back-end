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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@CrossOrigin("*")
@RestController
public class ProductsController {
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/api/client/products")
    public ResponseEntity<Object> getProductShopPages(
            @RequestParam(name = "brands_filter", required = false, defaultValue = "") List<Long> brandsFilter,
            @RequestParam(name = "categories_filter", required = false, defaultValue = "") List<Long> categoriesFilter,
            @RequestParam(name = "sizes_filter", required = false, defaultValue = "") List<Integer> sizesFilter,
            @RequestParam(name = "min_price", required = false, defaultValue = "") Long min,
            @RequestParam(name = "max_price", required = false, defaultValue = "") Long max,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page) {

        List<Long> brandIds = new ArrayList<>();
        List<Long> categoryIds = new ArrayList<>();
        List<Integer> sizes = new ArrayList<>();
        Long minPrice = (long) 0;
        Long maxPrice = Long.MAX_VALUE;

        //Lấy danh sách nhãn hiệu
        if (brandsFilter.size() > 0) {
            for (Long brandId : brandsFilter) {
                System.out.println(brandId);
                brandIds.add(brandId);
            }
        } else {
            List<Brand> brands = brandService.getListBrand();
            for (Brand brand : brands) {
                brandIds.add(brand.getId());
            }
        }

        //Lấy danh sách danh mục
        if (categoriesFilter.size() > 0) {
            for (Long categoryId : categoriesFilter) {
                categoryIds.add(categoryId);
            }
        } else {
            List<Category> categories = categoryService.getListCategories();
            for (Category category : categories) {
                categoryIds.add(category.getId());
            }
        }

        if (sizesFilter.size() > 0) {
            for (Integer size : sizesFilter) {
                sizes.add(size);
            }
        }

        //Lấy danh sách sản phẩm
        FilterProductRequest req = new FilterProductRequest(brandIds, categoryIds, sizes, minPrice, maxPrice, page);
        PageableDTO result = productService.filterProduct(req);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/api/client/products")
    public ResponseEntity<Object> getProductShopPages(@RequestBody(required = false) FilterProductRequest fil) {

        List<Long> brandIds = new ArrayList<>();
        List<Long> categoryIds = new ArrayList<>();
        List<Integer> sizes = new ArrayList<>();
        Long minPrice = (long) 0;
        Long maxPrice = Long.MAX_VALUE;
        int page = 3;
        if(Objects.isNull(fil)){
            List<Brand> brands = brandService.getListBrand();
            for (Brand brand : brands) {
                brandIds.add(brand.getId());
            }
            List<Category> categories = categoryService.getListCategories();
            for (Category category : categories) {
                categoryIds.add(category.getId());
            }
        }else{
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
        }
        // danh sách sản phẩm
        FilterProductRequest req = new FilterProductRequest(brandIds, categoryIds, sizes, minPrice, maxPrice, page);
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
    @GetMapping("/api/client/find")
    public ResponseEntity<Object> findProduct(@RequestParam("keywords") Optional<String> keyword, @RequestParam("page")Optional<Integer> page) {
        PageableDTO result = productService.findProductbyName(keyword, page);
        return ResponseEntity.ok(result);
    }
}
