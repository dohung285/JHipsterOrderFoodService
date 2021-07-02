package com.dohung.orderfood.repository;

import com.dohung.orderfood.domain.OrderDetail;
import com.dohung.orderfood.domain.OrderIdentity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    //    List<OrderDetail> findAllBy(List<Integer> orderIds);

    @Query(value = "SELECT * FROM order_food_detail WHERE order_id IN (:ids)", nativeQuery = true)
    List<OrderDetail> findAllByIdIn(@Param("ids") List<Integer> ids);

    @Query(value = "DELETE from order_food_detail where order_id = :ids", nativeQuery = true)
    void deleteAllByOrderId(@Param("ids") Integer ids);

    void deleteAllById(OrderIdentity orderIdentity);
}
