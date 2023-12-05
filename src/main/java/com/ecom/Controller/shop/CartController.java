package com.ecom.Controller.shop;

import com.ecom.Entity.*;
import com.ecom.Exception.BadRequestException;
import com.ecom.Model.dto.CheckPromotion;
import com.ecom.Model.request.CreateOrderDetailsRequest;
import com.ecom.Model.request.CreateOrderRequest;
import com.ecom.Model.request.CreateOrdersRequest;
import com.ecom.Security.CustomUserDetails;
import com.ecom.Service.OrdersService;
import com.ecom.Service.PromotionService;
import com.ecom.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.sql.Timestamp;
import java.util.Objects;

@RestController
public class CartController {
    @Autowired
    private PromotionService promotionService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrdersService ordersService;


    @GetMapping("/api/check-hidden-promotion")
    public ResponseEntity<Object> checkPromotion(@RequestParam String code) {
        if (code == null || code == "") {
            throw new BadRequestException("Mã code trống");
        }

        Promotion promotion = promotionService.checkPromotion(code);
        if (promotion == null) {
            throw new BadRequestException("Mã code không hợp lệ");
        }

        if (promotion.getExpiredAt().compareTo(new Timestamp(System.currentTimeMillis())) < 0){
            throw new BadRequestException("Mã code đã hết hạn");
        }
        CheckPromotion checkPromotion = new CheckPromotion();
        checkPromotion.setDiscountType(promotion.getDiscountType());
        checkPromotion.setDiscountValue(promotion.getDiscountValue());
        checkPromotion.setMaximumDiscountValue(promotion.getMaximumDiscountValue());
        return ResponseEntity.ok(checkPromotion);
    }

//    @PostMapping("/api/orders")
//    public ResponseEntity<Object> createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest) {
//        User user = userService.findByEmail(createOrderRequest.getEmail());
//     if(Objects.isNull(user)){
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
//        Order order = orderService.createOrder(createOrderRequest, user.getId());
//
//        return ResponseEntity.ok(order.getId());
//    }

    @PostMapping("/api/orders")
    public ResponseEntity<Object> createOrder(@Valid @RequestBody CreateOrdersRequest createOrdersRequest) {
        User user = userService.findByEmail(createOrdersRequest.getEmail());
        if (Objects.isNull(user)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Orders order = ordersService.createOrder(createOrdersRequest, user.getId());
        return ResponseEntity.ok(order.getId());
    }

}
