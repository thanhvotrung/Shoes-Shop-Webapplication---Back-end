package com.ecom.Controller.admin;


import com.ecom.Entity.Category;
import com.ecom.Entity.Promotion;
import com.ecom.Model.mapper.CategoryMapper;
import com.ecom.Model.request.CreatePromotionRequest;
import com.ecom.Service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    @GetMapping("api/admin/promotions")
    public ResponseEntity<Object> getListPromotionPages(
                                        @RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "", required = false) String code,
                                        @RequestParam(defaultValue = "", required = false) String name,
                                        @RequestParam(defaultValue = "", required = false) String publish,
                                        @RequestParam(defaultValue = "", required = false) String active) {


        Page<Promotion> promotionPage = promotionService.adminGetListPromotion(code, name, publish, active, page);
        return ResponseEntity.ok(promotionPage);
    }

    @GetMapping("/api/admin/promotions/{id}")
    public ResponseEntity<Object> getCategoryById(@PathVariable long id) {
        Promotion promotion = promotionService.getPromotionById(id);
        return ResponseEntity.ok(promotion);
    };


    @PostMapping("/api/admin/promotions")
    public ResponseEntity<Object> createPromotion(@Valid @RequestBody CreatePromotionRequest createPromotionRequest) {
        Promotion promotion = promotionService.createPromotion(createPromotionRequest);
        return ResponseEntity.ok(promotion.getId());
    }

    @PutMapping("/api/admin/promotions/{id}")
    public ResponseEntity<Object> updatePromotion(@Valid @RequestBody CreatePromotionRequest createPromotionRequest, @PathVariable long id) {
        promotionService.updatePromotion(createPromotionRequest, id);
        return ResponseEntity.ok("Cập nhật thành công");
    }

    @DeleteMapping("/api/admin/promotions/{id}")
    public ResponseEntity<Object> deletePromotion(@PathVariable long id) {
        promotionService.deletePromotion(id);
        return ResponseEntity.ok("Xóa khuyến mại thành công");
    }
}
