package com.ecom.Repository;

import com.ecom.Entity.Statistic;
import com.ecom.Model.dto.StatisticDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatisticRepository extends JpaRepository<Statistic, Long> {

    @Query(name = "getStatistic30Day",nativeQuery = true)
    List<StatisticDTO> getStatistic30Day();

    @Query(name = "getStatisticDayByDay",nativeQuery = true)
    List<StatisticDTO> getStatisticDayByDay(String toDate, String formDate);

    @Query(value = "SELECT * FROM statistic  WHERE date_format(created_at,'%Y-%m-%d') = date_format(NOW(),'%Y-%m-%d')",nativeQuery = true)
    Statistic findByCreatedAT();


}
