package com.scand.bookshop.repository;

import com.scand.bookshop.entity.SaleStat;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SaleStatRepository extends JpaRepository<SaleStat, Long> {

  @Query("SELECT s FROM SaleStat s WHERE s.date >= :startDate")
  List<SaleStat> findStatsForLastDays(LocalDate startDate);
}
