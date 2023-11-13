package com.ecom.Controller.shop;

import com.ecom.Entity.Brand;
import com.ecom.Entity.Category;
import com.ecom.Exception.NotFoundException;
import com.ecom.Model.dto.DetailProductInfoDTO;
import com.ecom.Model.dto.PageableDTO;
import com.ecom.Model.dto.ProductInfoDTO;
import com.ecom.Model.request.FilterProductRequest;
import com.ecom.Service.BrandService;
import com.ecom.Service.CategoryService;
import com.ecom.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HomeController {
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/api/product/new-products")
    public ResponseEntity<Object> newProducts() {
        List<ProductInfoDTO> newProducts = productService.getListNewProducts();
        return ResponseEntity.ok(newProducts);
    }

    @GetMapping("/api/product/top-view-products")
    public ResponseEntity<Object> topViewProducts() {
        List<ProductInfoDTO> viewProducts = productService.getListViewProducts();
        return ResponseEntity.ok(viewProducts);
    }

    @GetMapping("/api/product/top-sale-products")
    public ResponseEntity<Object> topSaleProducts() {
        List<ProductInfoDTO> bestSellerProducts = productService.getListBestSellProducts();
        return ResponseEntity.ok(bestSellerProducts);
    }

    @GetMapping("/api/{slug}/{id}")
    public ResponseEntity<Object> getProductDetail(@PathVariable String id) {
        DetailProductInfoDTO product = productService.getDetailProductById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/{id}")
    public ResponseEntity<Object> getProductDetail2(@PathVariable String id) {
        DetailProductInfoDTO product = productService.getDetailProductById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/product/sizes/{id}")
    public ResponseEntity<Object> getAvailableSizes(@PathVariable String id) {
        List<Integer> availableSizes = productService.getListAvailableSize(id);
        return ResponseEntity.ok(availableSizes);
    }

    //Lấy sản phẩm liên quan
    @GetMapping("/api/product/related-products/{id}")
    public ResponseEntity<Object> relatedProducts(@PathVariable String id) {
        List<ProductInfoDTO> relatedProducts = productService.getRelatedProducts(id);
        return ResponseEntity.ok(relatedProducts);
    }

    @PostMapping("/api/san-pham/loc")
    public ResponseEntity<?> filterProduct(@RequestBody(required = false) FilterProductRequest req) {
        // Validate
        if (req.getMinPrice() == null) {
            req.setMinPrice((long) 0);
        } else {
            if (req.getMinPrice() < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mức giá phải lớn hơn 0");
            }
        }
        if (req.getMaxPrice() == null) {
            req.setMaxPrice(Long.MAX_VALUE);
        } else {
            if (req.getMaxPrice() < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mức giá phải lớn hơn 0");
            }
        }

        PageableDTO result = productService.filterProduct(req);

        return ResponseEntity.ok(result);
    }
}
