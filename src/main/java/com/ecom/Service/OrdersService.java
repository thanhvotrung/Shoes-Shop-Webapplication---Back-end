package com.ecom.Service;

import com.ecom.Entity.*;
import com.ecom.Exception.BadRequestException;
import com.ecom.Exception.InternalServerException;
import com.ecom.Exception.NotFoundException;
import com.ecom.Model.dto.OrderDetailDTO;
import com.ecom.Model.dto.OrderInfoDTO;
import com.ecom.Model.request.CreateOrderDetailsRequest;
import com.ecom.Model.request.CreateOrdersRequest;
import com.ecom.Model.request.UpdateStatusOrderRequest;
import com.ecom.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ecom.config.Contant.*;

@Service
public class OrdersService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductSizeRepository productSizeRepository;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OrderDetailsRepository ordersDetailsRepository;
    @Autowired
    private PromotionService promotionService;
    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private StatisticRepository statisticRepository;

    public Page<Orders> adminGetListOrders(String id, String name, String phone, String status, int page) {
        page--;
        if (page < 0) {
            page = 0;
        }
        int limit = 10;
        Pageable pageable = PageRequest.of(page, limit, Sort.by("created_at").descending());
        return ordersRepository.adminGetListOrder(id, name, phone, status, pageable);
    }

    public Orders createOrder(CreateOrdersRequest createOrdersRequest, long userId) {
        List<OrderDetails> orderDetailsList = new ArrayList<>();
        List<CreateOrderDetailsRequest> products = createOrdersRequest.getProducts();
        Orders order = new Orders();
        User user = new User();
        user.setId(userId);
        order.setCreatedBy(user);
        order.setBuyer(user);
        order.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        order.setReceiverAddress(createOrdersRequest.getReceiverAddress());
        order.setReceiverName(createOrdersRequest.getReceiverName());
        order.setReceiverPhone(createOrdersRequest.getReceiverPhone());
        order.setNote(createOrdersRequest.getNote());
        order.setSubtotalPrice(createOrdersRequest.getSubtotalPrice());
        order.setTotalPrice(createOrdersRequest.getTotalPrice());
        order.setStatus(ORDER_STATUS);

        Optional<Promotion> promotion = promotionRepository.findByCouponCode(createOrdersRequest.getCouponCode());
        if (!promotion.isEmpty()) {
            Orders.UsedPromotion promotionUsed = new Orders.UsedPromotion();
            promotionUsed.setCouponCode(createOrdersRequest.getCouponCode());
            promotionUsed.setDiscountType(promotion.get().getDiscountType());
            promotionUsed.setDiscountValue(promotion.get().getDiscountValue());
            promotionUsed.setMaximumDiscountValue(promotion.get().getMaximumDiscountValue());
            order.setPromotion(promotionUsed);
        }
        for (CreateOrderDetailsRequest product : products) {
            //Kiểm tra sản phẩm có tồn tại
            Optional<Product> productOptional = productRepository.findById(product.getProductId());
            if (productOptional.isEmpty()) {
                throw new NotFoundException("Sản phẩm không tồn tại!");
            }
            //Kiểm tra size có sẵn
            ProductSize productSize = productSizeRepository.checkProductAndSizeAvailable(product.getProductId(), product.getSize());
            if (productSize == null || productSize.getQuantity() == 0) {
                throw new BadRequestException("Size giày " + productOptional.get().getName() + " sản phẩm tạm hết, Vui lòng chọn sản phẩm khác!");
            }
            if (product.getQuantity() > productSize.getQuantity()) {
                throw new BadRequestException("Số lượng giày " + productOptional.get().getName() + " không đủ, vui lòng chọn size hoặc sản phẩm khác!");
            }
            System.out.println(product.getProductPrice());
            System.out.println(productOptional.get().getSalePrice());
            //Kiểm tra giá sản phẩm
            if (productOptional.get().getSalePrice() != product.getProductPrice()) {
                throw new BadRequestException("Giá sản phẩm " + productOptional.get().getName() + " thay đổi, Vui lòng đặt hàng lại!");
            }
            OrderDetails orderDetails = new OrderDetails();
            ;
            orderDetails.setSize(product.getSize());
            orderDetails.setPrice(product.getProductPrice());
            orderDetails.setQuantity(product.getQuantity());
            orderDetails.setProduct(productOptional.get());
            orderDetailsList.add(orderDetails);
        }

        ordersRepository.save(order);
        for (OrderDetails orderDetails : orderDetailsList) {
            orderDetails.setOrdersId(order.getId());
            ordersDetailsRepository.save(orderDetails);
            //Trừ đi một sản phẩm
            productSizeRepository.minusProductBySize(orderDetails.getProduct().getId(), orderDetails.getSize(), orderDetails.getQuantity());
        }
        return order;
    }

    public List<OrderInfoDTO> getListOrderOfPersonByStatus(int status, long userId) {
        List<OrderInfoDTO> list = ordersRepository.getListOrderOfPersonByStatus(status, userId);
        return list;
    }

    public List<OrderDetailDTO> getOrderDetailsById(long id, long userId) {
        List<OrderDetailDTO> order = ordersRepository.getOrderDetailsById(id, userId);
        if (order.isEmpty()) {
            return null;
        }

        if (order.get(0).getStatus() == ORDER_STATUS) {
            order.get(0).setStatusText("Chờ lấy hàng");
        } else if (order.get(0).getStatus() == DELIVERY_STATUS) {
            order.get(0).setStatusText("Đang giao hàng");
        } else if (order.get(0).getStatus() == COMPLETED_STATUS) {
            order.get(0).setStatusText("Đã giao hàng");
        } else if (order.get(0).getStatus() == CANCELED_STATUS) {
            order.get(0).setStatusText("Đơn hàng đã hủy");
        } else if (order.get(0).getStatus() == RETURNED_STATUS) {
            order.get(0).setStatusText("Đơn hàng đã trả lại");
        }
        return order;
    }

    public void userCancelOrder(long id, long userId) {
        Optional<Orders> rs = ordersRepository.findById(id);
        List<OrderDetails> orderDetailsList = ordersDetailsRepository.findAllByOrdersId(id);
        if (rs.isEmpty()) {
            throw new NotFoundException("Đơn hàng không tồn tại");
        }
        Orders order = rs.get();
        if (order.getBuyer().getId() != userId) {
            throw new BadRequestException("Bạn không phải chủ nhân đơn hàng");
        }
        if (order.getStatus() != ORDER_STATUS) {
            throw new BadRequestException("Trạng thái đơn hàng không phù hợp để hủy. Vui lòng liên hệ với shop để được hỗ trợ");
        }

        order.setStatus(CANCELED_STATUS);
        ordersRepository.save(order);

        for (OrderDetails orderDetails : orderDetailsList) {
            //Cộng lại sản phẩm
            productSizeRepository.plusProductBySize(orderDetails.getProduct().getId(), orderDetails.getSize(), orderDetails.getQuantity());
        }

    }

    public void updateStatusDeliveryOrder(long orderId, long userId) {
        Optional<Orders> rs = ordersRepository.findById(orderId);
        if (rs.isEmpty()) {
            throw new NotFoundException("Đơn hàng không tồn tại");
        }
        Orders order = rs.get();
        User user = new User();
        user.setId(userId);
        order.setModifiedBy(user);
        order.setModifiedAt(new Timestamp(System.currentTimeMillis()));
        order.setStatus(2);
        try {
            ordersRepository.save(order);
        } catch (Exception e) {
            throw new InternalServerException("Lỗi khi cập nhật trạng thái");
        }
    }

    public void updateStatusCompletedOrder(long orderId, long userId) {
        Optional<Orders> rs = ordersRepository.findById(orderId);
        List<OrderDetails> orderDetailsList = ordersDetailsRepository.findAllByOrdersId(orderId);
        if (rs.isEmpty()) {
            throw new NotFoundException("Đơn hàng không tồn tại");
        }
        Orders order = rs.get();
        User user = new User();
        user.setId(userId);
        order.setModifiedBy(user);
        order.setModifiedAt(new Timestamp(System.currentTimeMillis()));
        order.setStatus(3);
        try {
            ordersRepository.save(order);
            for (OrderDetails orderDetails : orderDetailsList) {
                //cộng một sản phẩm vào sản phẩm đã bán và cộng tiền
                productRepository.plusOneProductTotalSold2(orderDetails.getProduct().getId(), orderDetails.getQuantity());
            }
                statistic(order.getTotalPrice(), order);
        } catch (Exception e) {
            throw new InternalServerException("Lỗi khi cập nhật trạng thái");
        }
    }

    public void updateStatusOrder(UpdateStatusOrderRequest updateStatusOrderRequest, long orderId, long userId) {
        Optional<Orders> rs = ordersRepository.findById(orderId);
        if (rs.isEmpty()) {
            throw new NotFoundException("Đơn hàng không tồn tại");
        }
        Orders order = rs.get();
        List<OrderDetails> orderDetails = ordersDetailsRepository.findAllByOrdersId(orderId);

        //Kiểm tra trạng thái của đơn hàng
        boolean check = false;
        for (Integer status : LIST_ORDER_STATUS) {
            if (status == updateStatusOrderRequest.getStatus()) {
                check = true;
                break;
            }
        }
        if (!check) {
            throw new BadRequestException("Trạng thái đơn hàng không hợp lệ");
        }
        //Cập nhật trạng thái đơn hàng
        if (order.getStatus() == ORDER_STATUS) {
            //Đơn hàng ở trạng thái chờ lấy hàng
            if (updateStatusOrderRequest.getStatus() == ORDER_STATUS) {
                order.setReceiverPhone(updateStatusOrderRequest.getReceiverPhone());
                order.setReceiverName(updateStatusOrderRequest.getReceiverName());
                order.setReceiverAddress(updateStatusOrderRequest.getReceiverAddress());
                //Đơn hàng ở trạng thái đang vận chuyển
            } else if (updateStatusOrderRequest.getStatus() == DELIVERY_STATUS) {
                //Trừ đi một sản phẩm
//                productSizeRepository.minusOneProductBySize(order.getProduct().getId(), order.getSize());
                //Đơn hàng ở trạng thái đã giao hàng
            } else if (updateStatusOrderRequest.getStatus() == COMPLETED_STATUS) {
                for (OrderDetails details : orderDetails) {
                    //Trừ đi một sản phẩm và cộng một sản phẩm vào sản phẩm đã bán và cộng tiền
//                productSizeRepository.minusOneProductBySize(order.getProduct().getId(), order.getSize());
                    productRepository.plusOneProductTotalSold(details.getProduct().getId());
//                    statistic(order.getTotalPrice(), details.getQuantity(), order);
                }
            } else if (updateStatusOrderRequest.getStatus() != CANCELED_STATUS) {
                throw new BadRequestException("Không thế chuyển sang trạng thái này");
            }
            //Đơn hàng ở trạng thái đang giao hàng
        } else if (order.getStatus() == DELIVERY_STATUS) {
            //Đơn hàng ở trạng thái đã giao hàng
            if (updateStatusOrderRequest.getStatus() == COMPLETED_STATUS) {
                for (OrderDetails details : orderDetails) {
                    //Cộng một sản phẩm vào sản phẩm đã bán và cộng tiền
//                productSizeRepository.minusOneProductBySize(order.getProduct().getId(), order.getSize());
                    productRepository.plusOneProductTotalSold(details.getProduct().getId());
//                    statistic(order.getTotalPrice(), details.getQuantity(), order);
                }
                //Đơn hàng ở trạng thái đã hủy
            } else if (updateStatusOrderRequest.getStatus() == RETURNED_STATUS) {
                for (OrderDetails details : orderDetails) {
                    //Cộng lại một sản phẩm đã bị trừ
                    productSizeRepository.plusOneProductBySize(details.getProduct().getId(), details.getSize());
                    //Đơn hàng ở trạng thái đã trả hàng
                }
            } else if (updateStatusOrderRequest.getStatus() == CANCELED_STATUS) {
                for (OrderDetails details : orderDetails) {
                    //Cộng lại một sản phẩm đã bị trừ
                    productSizeRepository.plusOneProductBySize(details.getProduct().getId(), details.getSize());
                }

            } else if (updateStatusOrderRequest.getStatus() != DELIVERY_STATUS) {
                throw new BadRequestException("Không thế chuyển sang trạng thái này");
            }
            //Đơn hàng ở trạng thái đã giao hàng
        } else if (order.getStatus() == COMPLETED_STATUS) {
            //Đơn hàng đang ở trạng thái đã hủy
            if (updateStatusOrderRequest.getStatus() == RETURNED_STATUS) {
                for (OrderDetails details : orderDetails) {
                    //Cộng một sản phẩm đã bị trừ và trừ đi một sản phẩm đã bán và trừ số tiền
                    productSizeRepository.plusOneProductBySize(details.getProduct().getId(), details.getSize());
                    productRepository.minusOneProductTotalSold(details.getProduct().getId());
//                    updateStatistic(order.getTotalPrice(), details.getQuantity(), order);
                }

            } else if (updateStatusOrderRequest.getStatus() != COMPLETED_STATUS) {
                throw new BadRequestException("Không thế chuyển sang trạng thái này");
            }
        } else {
            if (order.getStatus() != updateStatusOrderRequest.getStatus()) {
                throw new BadRequestException("Không thế chuyển đơn hàng sang trạng thái này");
            }
        }

        User user = new User();
        user.setId(userId);
        order.setModifiedBy(user);
        order.setModifiedAt(new Timestamp(System.currentTimeMillis()));
        order.setNote(updateStatusOrderRequest.getNote());
        order.setStatus(updateStatusOrderRequest.getStatus());
        try {
            ordersRepository.save(order);
        } catch (Exception e) {
            throw new InternalServerException("Lỗi khi cập nhật trạng thái");
        }
    }

    public void statistic(long amount, Orders orders) {
        Statistic statistic = statisticRepository.findByCreatedAT();
        List<OrderDetails> orderDetailsList = ordersDetailsRepository.findAllByOrdersId(orders.getId());
        if (statistic != null) {
            statistic.setOrders(orders);
            statistic.setSales(statistic.getSales() + amount);
            for (OrderDetails orderDetails : orderDetailsList) {
                Optional<Product> product = productRepository.findById(orderDetails.getProduct().getId());
                if(product.isEmpty()) throw new NotFoundException("Sản phẩm không tồn tại.");
                statistic.setQuantity(statistic.getQuantity() + orderDetails.getQuantity());
                statistic.setProfit(statistic.getProfit() + (amount - orderDetails.getQuantity() * product.get().getPrice()));
            }
            statisticRepository.save(statistic);
        } else {
            Statistic statistic1 = new Statistic();
            statistic1.setOrders(orders);
            statistic1.setSales(amount);
            for (OrderDetails orderDetails : orderDetailsList) {
                Optional<Product> product = productRepository.findById(orderDetails.getProduct().getId());
                if(product.isEmpty()) throw new NotFoundException("Sản phẩm không tồn tại.");
                statistic1.setQuantity(statistic1.getQuantity() + orderDetails.getQuantity());
                statistic1.setProfit(amount - (orderDetails.getQuantity() * product.get().getPrice()));
            }
            statistic1.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            statisticRepository.save(statistic1);
        }
    }

//    public void updateStatistic(long amount, int quantity, Orders order) {
//        Statistic statistic = statisticRepository.findByCreatedAT();
//        if (statistic != null) {
//            statistic.setOrder(order);
//            statistic.setSales(statistic.getSales() - amount);
//            statistic.setQuantity(statistic.getQuantity() - quantity);
//            statistic.setProfit(statistic.getSales() - (statistic.getQuantity() * order.getProduct().getPrice()));
//            statisticRepository.save(statistic);
//        }
//    }

    public long getCountOrder() {
        return ordersRepository.count();
    }
}
