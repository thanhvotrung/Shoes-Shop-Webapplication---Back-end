package com.ecom.Repository;

import com.ecom.Entity.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductSizeRepository extends JpaRepository<ProductSize,Long> {

    //Lấy size của sản phẩm
    @Query(nativeQuery = true,value = "SELECT ps.size FROM product_size ps WHERE ps.product_id = ?1 AND ps.quantity > 0")
    List<Integer> findAllSizeOfProduct(String id);

    List<ProductSize> findByProductId(String id);

    //Kiểm trả size sản phẩm
    @Query(value = "SELECT * FROM product_size WHERE product_id = ?1 AND size = ?2 AND quantity >0", nativeQuery = true)
    ProductSize checkProductAndSizeAvailable(String id, int size);

    //Trừ 1 sản phẩm theo size
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "Update product_size set quantity = quantity - 1 where product_id = ?1 and size = ?2")
    public void minusOneProductBySize(String id, int size);

    //Cộng 1 sản phẩm theo size
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "Update product_size set quantity = quantity + 1 where product_id = ?1 and size = ?2")
    public void plusOneProductBySize(String id, int size);

//    @Query(value = "SELECT * FROM product_size ps WHERE ps.size = ?1 AND ps.product_id = ?2",nativeQuery = true)
//    Optional<ProductSize> getProductSizeBySize(int size,String productId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "Delete from product_size where product_id = ?1")
    public void deleteByProductId(String id);
}
