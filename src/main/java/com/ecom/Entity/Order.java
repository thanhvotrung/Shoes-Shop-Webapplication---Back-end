package com.ecom.Entity;


import com.ecom.Model.dto.OrderDetailDTO;
import com.ecom.Model.dto.OrderInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;
//@SqlResultSetMappings(
//        value = {
//                @SqlResultSetMapping(
//                        name = "orderInfoDTO",
//                        classes = @ConstructorResult(
//                                targetClass = OrderInfoDTO.class,
//                                columns = {
//                                        @ColumnResult(name = "id", type = Long.class),
//                                        @ColumnResult(name = "total_price", type = Long.class),
//                                        @ColumnResult(name = "size_vn", type = Integer.class),
//                                        @ColumnResult(name = "product_name", type = String.class),
//                                        @ColumnResult(name = "product_img", type = String.class)
//                                }
//                        )
//                ),
//                @SqlResultSetMapping(
//                        name = "orderDetailDto",
//                        classes = @ConstructorResult(
//                                targetClass = OrderDetailDTO.class,
//                                columns = {
//                                        @ColumnResult(name = "id", type = Long.class),
//                                        @ColumnResult(name = "total_price", type = Long.class),
//                                        @ColumnResult(name = "product_price", type = Long.class),
//                                        @ColumnResult(name = "receiver_name", type = String.class),
//                                        @ColumnResult(name = "receiver_phone", type = String.class),
//                                        @ColumnResult(name = "receiver_address", type = String.class),
//                                        @ColumnResult(name = "status", type = Integer.class),
//                                        @ColumnResult(name = "size_vn", type = Integer.class),
//                                        @ColumnResult(name = "product_name", type = String.class),
//                                        @ColumnResult(name = "product_img", type = String.class)
//                                }
//                        )
//                )
//        }
//)
//@NamedNativeQuery(
//        name = "getListOrderOfPersonByStatus",
//        resultSetMapping = "orderInfoDTO",
//        query = "SELECT od.id, od.total_price, od.size size_vn, p.name product_name, (p.images ->> '$[0]') as product_img " +
//                "FROM orders od " +
//                "INNER JOIN product p " +
//                "ON od.product_id = p.id " +
//                "WHERE od.status = ?1 " +
//                "AND od.buyer =?2"
//)
//@NamedNativeQuery(
//        name = "userGetDetailById",
//        resultSetMapping = "orderDetailDto",
//        query = "SELECT orders.id, orders.total_price, orders.size size_vn, product.name product_name, orders.price as product_price, " +
//                "orders.receiver_name, orders.receiver_phone, orders.receiver_address, orders.status, " +
//                "product.images ->> \"$[0]\" as product_img " +
//                "FROM orders " +
//                "INNER JOIN product " +
//                "ON orders.product_id = product.id " +
//                "WHERE orders.id = ?1 AND orders.buyer = ?2"
//)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "receiver_name")
    private String receiverName;
    @Column(name = "receiver_phone")
    private String receiverPhone;
    @Column(name = "receiver_address")
    private String receiverAddress;
    @Column(name = "note")
    private String note;
    @Column(name = "price")
    private long price;
    @Column(name = "total_price")
    private long totalPrice;
    @Column(name = "size")
    private int size;
    @Column(name = "quantity")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "buyer")
    private User buyer;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "status")
    private int status;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "modified_at")
    private Timestamp modifiedAt;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "modified_by")
    private User modifiedBy;

    @Type(type = "json")
    @Column(name = "promotion", columnDefinition = "json")
    private UsedPromotion promotion;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UsedPromotion {
        private String couponCode;

        private int discountType;

        private long discountValue;

        private long maximumDiscountValue;
    }

}
