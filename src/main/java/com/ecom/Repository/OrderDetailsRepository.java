package com.ecom.Repository;

import com.ecom.Entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
    @Query(value = "select count(product_id) AS A from order_details where product_id = ?1;", nativeQuery = true)
    int countByProductIds(String id);

    List<OrderDetails> findAllByOrdersId(long id);

}
