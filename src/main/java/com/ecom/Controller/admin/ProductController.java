package com.ecom.Controller.admin;

import com.ecom.Entity.Product;
import com.ecom.Entity.ProductSize;
import com.ecom.Model.request.CreateProductRequest;
import com.ecom.Model.request.CreateSizeCountRequest;
import com.ecom.Model.request.UpdateFeedBackRequest;
import com.ecom.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/api/admin/products")
    public ResponseEntity<Object> getListProducts(@RequestParam(defaultValue = "", required = false) String id,
                                                  @RequestParam(defaultValue = "", required = false) String name,
                                                  @RequestParam(defaultValue = "", required = false) String category,
                                                  @RequestParam(defaultValue = "", required = false) String brand,
                                                  @RequestParam(defaultValue = "1", required = false) Integer page) {
        Page<Product> products = productService.adminGetListProduct(id, name, category, brand, page);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/api/admin/products/{id}")
    public ResponseEntity<Object> getProductDetail(@PathVariable String id) {
        Product rs = productService.getProductById(id);
        return ResponseEntity.ok(rs);
    }

    @PostMapping("/api/admin/products")
    public ResponseEntity<Object> createProduct(@Valid @RequestBody CreateProductRequest createProductRequest) {
        Product product = productService.createProduct(createProductRequest);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/api/admin/products/{id}")
    public ResponseEntity<Object> updateProduct(@Valid @RequestBody CreateProductRequest createProductRequest, @PathVariable String id) {
        productService.updateProduct(createProductRequest, id);
        return ResponseEntity.ok("Sửa sản phẩm thành công!");
    }

    @DeleteMapping("/api/admin/products")
    public ResponseEntity<Object> deleteProduct(@RequestBody String[] ids) {
        productService.deleteProduct(ids);
        return ResponseEntity.ok("Xóa sản phẩm thành công!");
    }

    @DeleteMapping("/api/admin/products/{id}")
    public ResponseEntity<Object> deleteProductById(@PathVariable String id) {
        productService.deleteProductById(id);
        return ResponseEntity.ok("Xóa sản phẩm thành công!");
    }

    @GetMapping("/api/admin/products/sizes/{id}")
    public ResponseEntity<Object> getSizeProduct(@PathVariable String id)
    {
        List<ProductSize> productSizes = productService.getListSizeOfProduct(id);
        return ResponseEntity.ok(productSizes);
    }

    @PutMapping("/api/admin/products/sizes")
    public ResponseEntity<?> updateSizeCount(@Valid @RequestBody CreateSizeCountRequest createSizeCountRequest) {
        productService.createSizeCount(createSizeCountRequest);

        return ResponseEntity.ok("Cập nhật thành công!");
    }

    @PutMapping("/api/admin/products/{id}/update-feedback-image")
    public ResponseEntity<?> updatefeedBackImages(@PathVariable String id, @Valid @RequestBody UpdateFeedBackRequest req) {
        productService.updatefeedBackImages(id, req);

        return ResponseEntity.ok("Cập nhật thành công");
    }



}
