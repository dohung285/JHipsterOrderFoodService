package com.dohung.orderfood.repository;

import com.dohung.orderfood.domain.OrderStatus;
import com.dohung.orderfood.web.rest.response.ObjectOrderStatusResponseDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer> {
    Optional<OrderStatus> findByOrderId(Integer orderId);

    @Query(
        "select new com.dohung.orderfood.web.rest.response.ObjectOrderStatusResponseDto( os.id,orf.address ,orf.phone,orf.username,orf.dateOrder,orf.note,os.status) from Order orf join OrderStatus os on orf.id = os.orderId  "
    )
    List<ObjectOrderStatusResponseDto> getAll();
}
