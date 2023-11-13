package com.ecom.Repository;

import com.ecom.Entity.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion,Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM promotion p " +
            "WHERE p.coupon_code LIKE CONCAT('%',?1,'%') " +
            "AND p.name LIKE CONCAT('%',?2,'%') " +
            "AND p.is_public LIKE CONCAT('%',?3,'%') " +
            "AND p.is_active LIKE CONCAT('%',?4,'%')")
    Page<Promotion> adminGetListPromotion(String code, String name, String publish, String active, Pageable pageable);

    //Kiểm tra có khuyến mại đang chạy
    @Query(nativeQuery = true, value = "SELECT * FROM promotion p WHERE p.is_active = true AND p.is_public = true AND expired_at > now()")
    Promotion checkHasPublicPromotion();

    //Lấy khuyến mại theo mã code
    Optional<Promotion> findByCouponCode(String code);

    //Lấy khuyến mại đang chạy theo mã code
    @Query(nativeQuery = true,value = "SELECT  * FROM promotion p WHERE p.is_active = true AND p.coupon_code = ?1")
    Promotion checkPromotion(String code);

    //Lấy khuyến mại đang chạy và còn thời hạn
    @Query(nativeQuery = true,value = "SELECT * FROM  promotion p WHERE p.expired_at > now() AND p.is_active = true")
    List<Promotion> getAllValidPromotion();

}
