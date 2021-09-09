package com.dohung.orderfood.repository;

import com.dohung.orderfood.domain.OrderDetail;
import com.dohung.orderfood.domain.OrderIdentity;
import com.dohung.orderfood.web.rest.response.ObjectOrderDetailOfUserResponseDto;
import java.util.List;
import javax.persistence.Tuple;
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

    //    @Query(value = "select new com.dohung.orderfood.web.rest.response.ObjectOrderDetailOfUserResponseDto( o.id, od.food_id, f.name, od.amount, od.money, i.path) FROM order_food_detail od left JOIN order_food o ON od.order_id = o.id left JOIN food f ON od.food_id = f.id Left JOIN image i ON f.image_id = i.id WHERE o.username = :username    ", nativeQuery = true)
    //    List<ObjectOrderDetailOfUserResponseDto> getAllOrderDetailByUsername(@Param("username") String username);

    @Query(
        value = "select new com.dohung.orderfood.web.rest.response.ObjectOrderDetailOfUserResponseDto( o.id, od.id.foodId, f.name, od.amount, od.money, i.path) FROM OrderDetail od left JOIN Order o ON od.id.orderId = o.id left JOIN Food f ON od.id.foodId = f.id Left JOIN Image i ON f.imageId = i.id WHERE o.username = :username    "
    )
    List<ObjectOrderDetailOfUserResponseDto> getAllOrderDetailByUsername(@Param("username") String username);

    @Query(
        value = "select  o.id, od.id.foodId, f.name, od.amount, od.money, i.path FROM OrderDetail od left JOIN Order o ON od.id.orderId = o.id left JOIN Food f ON od.id.foodId = f.id Left JOIN Image i ON f.imageId = i.id "
    )
    List<Tuple> getAllOrderDetail();

    @Query(
        value = "select  od.id.foodId, f.name, od.amount,d.percent, od.money FROM OrderDetail od left JOIN Food f ON od.id.foodId = f.id left JOIN Discount d on d.id = f.discountId where od.id.orderId = :orderId"
    )
    List<Tuple> getAllOrderDetailByOrderId(@Param("orderId") Integer orderId);
}
