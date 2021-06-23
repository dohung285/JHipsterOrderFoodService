package com.dohung.orderfood.repository;

import com.dohung.orderfood.domain.Bill;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {
    Optional<Bill> findAllByOrderId(Integer orderId);
}
