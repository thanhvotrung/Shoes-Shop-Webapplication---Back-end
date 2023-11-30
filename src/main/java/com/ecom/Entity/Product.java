package com.ecom.Entity;

import com.ecom.Model.dto.ChartDTO;
import com.ecom.Model.dto.ProductInfoDTO;
import com.ecom.Model.dto.ShortProductInfoDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
@SqlResultSetMappings(
        value ={
                @SqlResultSetMapping(
                        name ="productInfoDto",
                        classes =@ConstructorResult(
                                targetClass = ProductInfoDTO.class,
                                columns ={
                                        @ColumnResult(name = "id", type = String.class),
                                        @ColumnResult(name = "name", type = String.class),
                                        @ColumnResult(name = "slug", type = String.class),
                                        @ColumnResult(name = "price", type = Long.class),
                                        @ColumnResult(name = "views",type = Integer.class),
                                        @ColumnResult(name = "images", type = String.class),
                                        @ColumnResult(name = "total_sold", type = Integer.class)
                                }
                        )
                ),
                @SqlResultSetMapping(
                        name = "shortProductInfoDTO",
                        classes = @ConstructorResult(
                                targetClass = ShortProductInfoDTO.class,
                                columns = {
                                        @ColumnResult(name = "id", type = String.class),
                                        @ColumnResult(name = "name", type = String.class)
                                }
                        )
                ),
                @SqlResultSetMapping(
                        name = "productInfoAndAvailableSize",
                        classes = @ConstructorResult(
                                targetClass = ShortProductInfoDTO.class,
                                columns = {
                                        @ColumnResult(name = "id", type = String.class),
                                        @ColumnResult(name = "name", type = String.class),
                                        @ColumnResult(name = "price", type = Long.class),
                                        @ColumnResult(name = "sizes", type = String.class),

                                }
                        )
                ),
                @SqlResultSetMapping(
                        name = "chartProductDTO",
                        classes = @ConstructorResult(
                                targetClass = ChartDTO.class,
                                columns = {
                                        @ColumnResult(name = "label",type = String.class),
                                        @ColumnResult(name = "value",type = Integer.class)
                                }
                        )
                )
        }
)

@NamedNativeQuery(
        name = "getListNewProducts",
        resultSetMapping = "productInfoDto",
        query = "SELECT p.id, p.name, p.sale_price as price, p.product_view as views, p.slug, p.total_sold, p.images ->> '$[0]' AS images " +
                "FROM product p WHERE p.status = 1 " +
                "order by p.created_at DESC limit ?1"
)
@NamedNativeQuery(
        name = "getListBestSellProducts",
        resultSetMapping = "productInfoDto",
        query = "SELECT p.id, p.name, p.sale_price as price, p.product_view as views, p.slug, p.total_sold, p.images ->> '$[0]' AS images " +
                "FROM product p " +
                "WHERE p.status = 1 " +
                "ORDER BY total_sold DESC LIMIT ?1"
)

@NamedNativeQuery(
        name = "getListViewProducts",
        resultSetMapping = "productInfoDto",
        query = "SELECT p.id, p.name, p.sale_price as price, p.product_view as views, p.slug, p.total_sold, p.images ->> '$[0]' AS images " +
                "FROM product p " +
                "WHERE p.status = 1 " +
                "ORDER BY product_view DESC LIMIT ?1"
)

@NamedNativeQuery(
        name = "getRelatedProducts",
        resultSetMapping = "productInfoDto",
        query = "SELECT p.id, p.name, p.sale_price as price, p.product_view as views, p.slug, p.total_sold, p.images ->> '$[0]' AS images " +
                "FROM product p " +
                "WHERE p.status = 1 " +
                "AND p.id != ?1 " +
                "ORDER BY RAND() " +
                "LIMIT ?2"
)
@NamedNativeQuery(
        name = "getAllProduct",
        resultSetMapping = "shortProductInfoDTO",
        query = "SELECT p.id, p.name FROM product p"
)
@NamedNativeQuery(
        name = "getAllBySizeAvailable",
        resultSetMapping = "productInfoAndAvailableSize",
        query = "SELECT p.id, p.name, p.sale_price as price, " +
                "(SELECT JSON_ARRAYAGG(ps.size) FROM product_size ps WHERE ps.product_id = p.id AND ps.quantity > 0) AS sizes " +
                "FROM product p"
)
@NamedNativeQuery(
        name = "searchProductBySize",
        resultSetMapping = "productInfoDto",
        query = "SELECT DISTINCT d.* " +
                "FROM (" +
                "SELECT DISTINCT product.id, product.name, product.slug, product.sale_price as price, product.product_view as views, product.total_sold, product.images ->> '$[0]' AS images " +
                "FROM product " +
                "INNER JOIN product_category " +
                "ON product.id = product_category.product_id " +
                "WHERE product.status = 1 AND product.name LIKE CONCAT('%',?8,'%') AND product.brand_id IN (?1) AND product_category.category_id IN (?2) " +
                "AND product.sale_price BETWEEN ?3 AND ?4 " +
                "ORDER BY CASE WHEN ?9 = 'asc' THEN product.sale_price END ASC, CASE WHEN ?9 = 'desc' THEN product.sale_price END DESC) as d " +
                "INNER JOIN product_size " +
                "ON product_size.product_id = d.id " +
                "WHERE product_size.size IN (?5) AND product_size.quantity > 0 " +
                "LIMIT ?6 "+
                "OFFSET ?7"
)
@NamedNativeQuery(
        name = "searchProductAllSize",
        resultSetMapping = "productInfoDto",
        query = "SELECT DISTINCT product.id, product.name, product.slug, product.sale_price as price, product.product_view as views, product.total_sold, product.images ->> '$[0]' AS images " +
                "FROM product " +
                "INNER JOIN product_category " +
                "ON product.id = product_category.product_id " +
                "WHERE product.status = 1 AND product.name LIKE CONCAT('%',?7,'%') AND product.brand_id IN (?1) AND product_category.category_id IN (?2) " +
                "AND product.sale_price BETWEEN ?3 AND ?4 " +
                "ORDER BY CASE WHEN ?8 = 'asc' THEN product.sale_price END ASC, CASE WHEN ?8 = 'desc' THEN product.sale_price END DESC " + // Add this line for sorting+
                "LIMIT ?5 " +
                "OFFSET ?6"
)
@NamedNativeQuery(
        name = "searchProductByKeyword",
        resultSetMapping = "productInfoDto",
        query = "SELECT DISTINCT p.id, p.name, p.slug, p.sale_price as price, p.product_view as views, p.total_sold, p.images ->> '$[0]' AS images " +
                "FROM product p " +
                "INNER JOIN product_category pc " +
                "ON p.id = pc.product_id " +
                "INNER JOIN category c " +
                "ON c.id = pc.category_id " +
                "WHERE p.status = 1 AND (p.name LIKE CONCAT('%',:keyword,'%') OR c.name LIKE CONCAT('%',:keyword,'%')) " +
                "LIMIT :limit " +
                "OFFSET :offset "
)
@NamedNativeQuery(
        name = "getProductOrders",
        resultSetMapping = "chartProductDTO",
        query = "select p.name as label, sum(od.quantity) as value from product p " +
                "inner join order_details od on p.id = od.product_id " +
                "inner join orderss o on od.orders_id = o.id " +
                "where o.status = 3 and date_format(o.created_at,'%m') = ?1 " +
                "and date_format(o.created_at,'%Y') = ?2 " +
                "group by p.id order by sum(od.quantity) desc"
)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "product")
public class Product {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "name",nullable = false,length = 300)
    private String name;
    @Column(name = "description",columnDefinition = "TEXT")
    private String description;
    @Column(name = "price")
    private long price;
    @Column(name = "sale_price")
    private long salePrice;
    @Column(name = "slug",nullable = false)
    private String slug;
//    @JsonIgnore
    @Type(type = "json")
    @Column(name = "images",columnDefinition = "json")
    private ArrayList<String> images;
//    @JsonIgnore
    @Type(type = "json")
    @Column(name = "image_feedback",columnDefinition = "json")
    private ArrayList<String> imageFeedBack;
    @Column(name = "product_view")
    private int view;
    @Column(name = "total_sold")
    private long totalSold;
    @Column(name = "status")
    private int status;
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Column(name = "modified_at")
    private Timestamp modifiedAt;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToMany
    @JoinTable(
            name = "product_category",
            joinColumns =@JoinColumn(name = "product_id"),
            inverseJoinColumns =@JoinColumn(name = "category_id")
    )
    private List<Category> categories;

//    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
//    private List<CartProduct> cartProducts = new ArrayList<>();
@JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<Comment> comments;

//    @OneToMany(mappedBy = "product")
//    private List<Rate> rates;
}
