package com.ecom.Repository;

import com.ecom.Entity.Order;
import com.ecom.Entity.Orders;
import com.ecom.Model.dto.OrderDetailDTO;
import com.ecom.Model.dto.OrderInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
    @Query(value = "SELECT * FROM orderss " +
            "WHERE id LIKE CONCAT('%',?1,'%') " +
            "AND receiver_name LIKE CONCAT('%',?2,'%') " +
            "AND receiver_phone LIKE CONCAT('%',?3,'%') " +
            "AND status LIKE CONCAT('%',?4,'%') ", nativeQuery = true)
    Page<Orders> adminGetListOrder(String id, String name, String phone, String status, Pageable pageable);

    @Query( nativeQuery = true)
    List<OrderInfoDTO> getListOrderOfPersonByStatus(int status, long userId);

    @Query(nativeQuery = true)
    List<OrderDetailDTO> getOrderDetailsById(long id, long userId);
}
