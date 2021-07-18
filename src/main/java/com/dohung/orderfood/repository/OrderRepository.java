package com.dohung.orderfood.repository;

import com.dohung.orderfood.domain.Order;
import com.dohung.orderfood.web.rest.response.OrderOfUserResponseDto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query(
        "select new com.dohung.orderfood.web.rest.response.OrderOfUserResponseDto(orf.id,orf.dateOrder,os.status,b.totalMoney) from Order orf left join OrderStatus os on orf.id = os.orderId join Bill b on orf.id=b.orderId where orf.username= :username   "
    )
    List<OrderOfUserResponseDto> getAllOrderByUsername(@Param("username") String username);
}
