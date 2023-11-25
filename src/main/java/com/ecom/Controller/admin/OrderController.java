package com.ecom.Controller.admin;

import com.ecom.Entity.Orders;
import com.ecom.Entity.User;
import com.ecom.Security.CustomUserDetails;
import com.ecom.Service.OrdersService;
import com.ecom.Service.PromotionService;
import com.ecom.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrdersService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private PromotionService promotionService;

    @GetMapping("/api/admin/orders")
    public ResponseEntity<Object> getListOrderPage(@RequestParam(defaultValue = "1") Integer page,
                                                   @RequestParam(defaultValue = "", required = false) String id,
                                                   @RequestParam(defaultValue = "", required = false) String name,
                                                   @RequestParam(defaultValue = "", required = false) String phone,
                                                   @RequestParam(defaultValue = "", required = false) String status) {

        //Lấy danh sách đơn hàng
        Page<Orders> orderPage = orderService.adminGetListOrders(id, name, phone, status, page);
        return ResponseEntity.ok(orderPage);
    }

//    @PutMapping("/api/admin/orders/update-status/{id}")
//    public ResponseEntity<Object> updateStatusOrder(@Valid @RequestBody UpdateStatusOrderRequest updateStatusOrderRequest, @PathVariable long id) {
//        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
//        orderService.updateStatusOrder(updateStatusOrderRequest, id, user.getId());
//        return ResponseEntity.ok("Cập nhật trạng thái thành công");
//    }

    @PutMapping("/api/admin/orders/update-delivery/{id}")
    public ResponseEntity<Object> updateStatusDeliveryOrder(@PathVariable long id, @RequestBody String email) {
        User user = userService.findByEmail(email);
        if (Objects.isNull(user)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        orderService.updateStatusDeliveryOrder(id, user.getId());
        return ResponseEntity.ok("Cập nhật trạng thái thành công!");
    }

    @PutMapping("/api/admin/orders/update-completed/{id}")
    public ResponseEntity<Object> updateStatusCompletedOrder(@PathVariable long id, @RequestBody String email) {
        User user = userService.findByEmail(email);
        if (Objects.isNull(user)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        orderService.updateStatusCompletedOrder(id, user.getId());
        return ResponseEntity.ok("Cập nhật trạng thái thành công!");
    }

    @GetMapping("/api/test")
    public ResponseEntity<Object> test() {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok("Cập nhật trạng thái thành công");
    }
}
