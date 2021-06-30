package com.dohung.orderfood.repository;

import com.dohung.orderfood.domain.OrderDetail;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    List<OrderDetail> findAllByIdIn(List<Integer> orderIds);
}
