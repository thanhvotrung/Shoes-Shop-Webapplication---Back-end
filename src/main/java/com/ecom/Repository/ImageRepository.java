package com.ecom.Repository;

import com.ecom.Entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image,String> {
    Image findByLink(String link);

    @Query(nativeQuery = true, value = "SELECT link FROM images WHERE created_by = ?1")
    List<String> getListImageOfUser(long userId);

    @Query(value = "SELECT 1 FROM brand b WHERE b.thumbnail =?1",nativeQuery = true)
    Integer checkImageInUse(String link);
}
