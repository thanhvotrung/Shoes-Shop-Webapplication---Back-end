package com.ecom.Entity;

import com.ecom.Model.dto.StatisticDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@SqlResultSetMappings(
        value = {
                @SqlResultSetMapping(
                        name = "statisticDTO",
                        classes = @ConstructorResult(
                                targetClass = StatisticDTO.class,
                                columns = {
                                        @ColumnResult(name = "sales",type = Long.class),
                                        @ColumnResult(name = "profit",type = Long.class),
                                        @ColumnResult(name = "quantity",type = Integer.class),
                                        @ColumnResult(name = "createdAt",type = String.class)
                                }
                        )
                )
        }
)

@NamedNativeQuery(
        name = "getStatistic30Day",
        resultSetMapping = "statisticDTO",
        query = "SELECT s.sales, s.profit, s.quantity, date_format(s.created_at,'%Y-%m-%d') as createdAt FROM statistic s WHERE date_format(s.created_at,'%Y-%m-%d') BETWEEN DATE_SUB(NOW(), INTERVAL 30 DAY) AND NOW() ORDER BY createdAt ASC "
)
@NamedNativeQuery(
        name = "getStatisticDayByDay",
        resultSetMapping = "statisticDTO",
        query = "SELECT s.sales, s.profit, s.quantity, date_format(s.created_at,'%Y-%m-%d') as createdAt FROM statistic s WHERE date_format(s.created_at,'%Y-%m-%d') >=?1 AND date_format(s.created_at,'%Y-%m-%d') <=?2 ORDER BY createdAt ASC "
)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "statistic")
public class Statistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "sales")
    private long sales;
    @Column(name = "profit")
    private long profit;
    @Column(name = "quantity")
    private int quantity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    @Column(name = "created_at")
    private Timestamp createdAt;
}
