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

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity

@SqlResultSetMappings(
        value = {
                @SqlResultSetMapping(
                        name = "OrderInfoDTO",
                        classes = {
                                @ConstructorResult(
                                        targetClass = OrderInfoDTO.class,
                                        columns = {
                                                @ColumnResult(name = "id", type = Long.class),
                                                @ColumnResult(name = "total_price", type = Long.class),
                                                @ColumnResult(name = "product_img", type = String.class),
                                                @ColumnResult(name = "name", type = String.class),
                                                @ColumnResult(name = "size", type = Integer.class),
                                                @ColumnResult(name = "quantity", type = Integer.class),
                                                @ColumnResult(name = "price", type = Long.class),
                                                @ColumnResult(name = "count", type = Long.class)
                                        }
                                )
                        }
                ),
                @SqlResultSetMapping(
                        name = "OrderDetailDTO",
                        classes = {
                                @ConstructorResult(
                                        targetClass = OrderDetailDTO.class,
                                        columns = {
                                                @ColumnResult(name = "id", type = Long.class),
                                                @ColumnResult(name = "subtotal_price", type = Long.class),
                                                @ColumnResult(name = "total_price", type = Long.class),
                                                @ColumnResult(name = "receiver_name", type = String.class),
                                                @ColumnResult(name = "receiver_phone", type = String.class),
                                                @ColumnResult(name = "receiver_address", type = String.class),
                                                @ColumnResult(name = "product_img", type = String.class),
                                                @ColumnResult(name = "name", type = String.class),
                                                @ColumnResult(name = "size", type = Integer.class),
                                                @ColumnResult(name = "quantity", type = Integer.class),
                                                @ColumnResult(name = "price", type = Long.class),
                                                @ColumnResult(name = "status", type = Integer.class),
                                                @ColumnResult(name = "note", type = String.class)
                                        }
                                )
                        }
                )
        })
@NamedNativeQuery(
        name = "Orders.getListOrderOfPersonByStatus",
        resultSetMapping = "OrderInfoDTO",
        query = "SELECT od.id, od.total_price,(p.images ->> '$[0]') as product_img, p.name, de.size, de.quantity, de.price, count(od.id) as count " +
                "FROM  orderss as od " +
                "inner join order_details as de on od.id = de.orders_id " +
                "inner join product as p on de.product_id = p.id " +
                "WHERE od.status = ?1  AND od.buyer = ?2 group by od.id"
)
@NamedNativeQuery(
        name = "Orders.getOrderDetailsById",
        resultSetMapping = "OrderDetailDTO",
        query = "SELECT od.id, od.subtotal_price,od.receiver_name, od.receiver_phone, od.receiver_address, \n" +
                "od.total_price,(p.images ->> '$[0]') as product_img, p.name, de.size, de.quantity, de.price, od.status,\n" +
                "od.note \n" +
                "FROM  orderss as od \n" +
                "inner join order_details as de on od.id = de.orders_id \n" +
                "inner join product as p on de.product_id = p.id \n" +
                "WHERE od.id = ?1 AND od.buyer = ?2"
)

@Table(name = "orderss")
public class Orders {
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
    @Column(name = "subtotal_price")
    private long subtotalPrice;
    @Column(name = "total_price")
    private long totalPrice;

    @ManyToOne
    @JoinColumn(name = "buyer")
    private User buyer;

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
