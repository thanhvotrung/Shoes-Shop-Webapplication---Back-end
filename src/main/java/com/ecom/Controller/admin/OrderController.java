package com.ecom.Controller.admin;

import com.ecom.Entity.OrderDetails;
import com.ecom.Entity.Orders;
import com.ecom.Entity.User;
import com.ecom.Exception.NotFoundException;
import com.ecom.Model.dto.ShortProductInfoDTO;
import com.ecom.Model.request.UpdateDetailOrder;
import com.ecom.Model.request.UpdateStatusOrderRequest;
import com.ecom.Repository.OrderDetailsRepository;
import com.ecom.Security.CustomUserDetails;
import com.ecom.Service.OrdersService;
import com.ecom.Service.ProductService;
import com.ecom.Service.PromotionService;
import com.ecom.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.ecom.config.Contant.ORDER_STATUS;

@RestController
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrdersService orderService;
    @Autowired
    private OrderDetailsRepository ordersDetailsRepository;

    @Autowired
    private ProductService productService;

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

    //    Lấy thông tin đơn hàng trong admin
    @GetMapping("/api/admin/orders/info/{id}")
    public ResponseEntity<Object> updateInfoOrder(@PathVariable long id) {
        Orders order = orderService.findOrdersById(id);
        return ResponseEntity.ok(order);
    }

    //    Cập nhật thông tin đơn hàng trong admin
    @PutMapping("/api/admin/orders/update-info/{id}")
    public ResponseEntity<Object> updateStatusOrder(@Valid @RequestBody UpdateStatusOrderRequest updateStatusOrderRequest, @PathVariable long id) {
        User user = userService.findByEmail(updateStatusOrderRequest.getEmail());
        if (Objects.isNull(user)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        orderService.updateInfoOrder(updateStatusOrderRequest, id, user.getId());
        return ResponseEntity.ok("Cập nhật trạng thái thành công");
    }

    //    Lấy thông tin sản phẩm đơn hàng trong admin
    @GetMapping("/api/admin/orders/products/{id}")
    public ResponseEntity<Object> updateProductOrder(@PathVariable long id) {
        Orders order = orderService.findOrdersById(id);
        List<OrderDetails> orderDetailsList = ordersDetailsRepository.findAllByOrdersId(order.getId());
        return ResponseEntity.ok(orderDetailsList);
    }


//    public String updateOrderPage(@PathVariable long id) {
//
//        Orders order = orderService.findOrdersById(id);
//
//        if (order.getStatus() == ORDER_STATUS) {
//            // Get list product to select
//            List<ShortProductInfoDTO> products = productService.getAvailableProducts();
//            model.addAttribute("products", products);
//
//            // Get list valid promotion
//            List<Promotion> promotions = promotionService.getAllValidPromotion();
//            model.addAttribute("promotions", promotions);
//            if (order.getPromotion() != null) {
//                boolean validPromotion = false;
//                for (Promotion promotion : promotions) {
//                    if (promotion.getCouponCode().equals(order.getPromotion().getCouponCode())) {
//                        validPromotion = true;
//                        break;
//                    }
//                }
//                if (!validPromotion) {
//                    promotions.add(new Promotion(order.getPromotion()));
//                }
//            }
//
//            // Check size available
//            boolean sizeIsAvailable = productService.checkProductSizeAvailable(order.getProduct().getId(), order.getSize());
//            model.addAttribute("sizeIsAvailable", sizeIsAvailable);
//        }
//
//        return "admin/order/edit";
//    }


//    Cập nhập sản phẩm của đơn hàng trong admin
//    @PutMapping("/api/admin/orders/update-detail/{id}")
//    public ResponseEntity<Object> updateDetailOrder(@Valid @RequestBody UpdateDetailOrder detailOrder, @PathVariable long id) {
//        User user = userService.findByEmail(detailOrder.getEmail());
//        if (Objects.isNull(user)) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        orderService.updateDetailOrder(detailOrder, id, user.getId());
//        return ResponseEntity.ok("Cập nhật thành công");
//    }

    @GetMapping("/api/test")
    public ResponseEntity<Object> test(@CookieValue(name = "JWT_TOKEN", defaultValue = "DefaultCookie") String myCookie) {
        System.out.println(myCookie);
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        System.out.println(user.getFullName());


        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok("Cập nhật trạng thái thành công");
    }
}
