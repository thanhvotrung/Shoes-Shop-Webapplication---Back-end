package com.ecom.Controller.shop;

import com.ecom.Entity.User;
import com.ecom.Exception.BadRequestException;
import com.ecom.Model.dto.OrderDetailDTO;
import com.ecom.Model.dto.OrderInfoDTO;
import com.ecom.Service.OrdersService;
import com.ecom.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static com.ecom.config.Contant.LIST_ORDER_STATUS;

@RestController
public class OrdersController {
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private UserService userService;

    @GetMapping("/api/order-list")
    public ResponseEntity<Object> getListOrderByStatus(@RequestParam(required = false, defaultValue = "1") int status, @RequestParam String email) {
        // Validate status
        if (!LIST_ORDER_STATUS.contains(status)) {
            throw new BadRequestException("Trạng thái đơn hàng không hợp lệ");
        }
        User user = userService.findByEmail(email);
        if(Objects.isNull(user)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<OrderInfoDTO> orders = ordersService.getListOrderOfPersonByStatus(status, user.getId());

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/api/order-details")
    public ResponseEntity<Object> getOrderDetailsById(@RequestParam int id, @RequestParam String email) {
        User user = userService.findByEmail(email);
        if(Objects.isNull(user)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<OrderDetailDTO> orders = ordersService.getOrderDetailsById(id, user.getId());
        if(orders.isEmpty() || orders == null){
            return new ResponseEntity<>("Danh sách rỗng.",HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(orders);
    }

    @PostMapping("/api/cancel-order")
    public ResponseEntity<Object> cancelOrder(@RequestParam long id, @RequestParam String email) {
        User user = userService.findByEmail(email);
        if(Objects.isNull(user)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ordersService.userCancelOrder(id, user.getId());

        return ResponseEntity.ok("Hủy đơn hàng thành công.");
    }
}
