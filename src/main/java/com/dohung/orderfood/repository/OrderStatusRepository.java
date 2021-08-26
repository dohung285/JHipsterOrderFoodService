package com.dohung.orderfood.repository;

import com.dohung.orderfood.domain.OrderStatus;
import com.dohung.orderfood.web.rest.response.ObjectOrderStatusResponseDto;
import com.dohung.orderfood.web.rest.response.ObjectOrderStatusWithDateOrderRespone;
import java.util.List;
import java.util.Optional;
import javax.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer> {
    @Query("select ot from OrderStatus ot  where ot.orderId = :orderId ")
    Optional<OrderStatus> getByOrderId(@Param("orderId") Integer orderId);

    @Query(
        "select new com.dohung.orderfood.web.rest.response.ObjectOrderStatusResponseDto( orf.id,orf.address ,orf.phone,orf.username,orf.dateOrder,orf.note,os.status) from Order orf join OrderStatus os on orf.id = os.orderId  "
    )
    List<ObjectOrderStatusResponseDto> getAll();

    Optional<OrderStatus> findByOrderId(Integer id);
    //    Optional<OrderStatus> findAllByOrderId(Integer orderId);

    //    Optional<OrderStatus> findByOrderId(Integer orderId);
}
