package com.dohung.orderfood.repository;

import com.dohung.orderfood.domain.Discount;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Integer> {
    List<Discount> findAll();

    List<Discount> findAllByStartDateIsBeforeAndEndDateIsBefore(LocalDateTime startDate, LocalDateTime endDate);
}
