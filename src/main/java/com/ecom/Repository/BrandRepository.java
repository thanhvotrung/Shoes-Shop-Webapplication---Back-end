package com.ecom.Repository;

import com.ecom.Entity.Brand;
import com.ecom.Model.dto.ChartDTO;
import com.ecom.Model.dto.PostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;
@SqlResultSetMappings(
        value = {
                @SqlResultSetMapping(
                        name = "postInfoDto",
                        classes = @ConstructorResult(
                                targetClass = PostDTO.class,
                                columns = {
                                        @ColumnResult(name = "id", type = Long.class),
                                        @ColumnResult(name = "slug", type = String.class),
                                        @ColumnResult(name = "title", type = String.class),
                                        @ColumnResult(name = "thumbnail",type = String.class),
                                        @ColumnResult(name = "created_time", type = String.class),
                                        @ColumnResult(name = "published_time", type = String.class),
                                        @ColumnResult(name = "status", type = String.class)
                                }
                        )
                )
        }
)
@NamedNativeQuery(
        name = "adminGetListPost",
        resultSetMapping = "postInfoDto",
        query = "SELECT id, slug, title, thumbnail, " +
                "DATE_FORMAT(created_at,'%d/%m/%Y %H:%i') as created_time, " +
                "DATE_FORMAT(published_at,'%d/%m/%Y %H:%i') as published_time, " +
                "(CASE WHEN status = true " +
                "THEN 'Công khai' " +
                "ELSE 'Nháp' " +
                "END ) as status " +
                "FROM post " +
                "WHERE title LIKE CONCAT('%',:title,'%') AND status LIKE CONCAT('%',:status,'%') " +
                "ORDER BY created_time DESC " +
                "LIMIT :limit OFFSET :offset "
)
@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    Brand findByName(String name);

    @Query(value = "SELECT * FROM brand b " +
            "WHERE b.id LIKE CONCAT('%',?1,'%') " +
            "AND b.name LIKE CONCAT('%',?2,'%') " +
            "AND b.status LIKE CONCAT('%',?3,'%')", nativeQuery = true)
    Page<Brand> adminGetListBrands(String id, String name, String status, Pageable pageable);

    @Query(name = "getProductOrderBrands",nativeQuery = true)
    List<ChartDTO> getProductOrderBrands();

}
