package com.dohung.orderfood.repository;

import com.dohung.orderfood.domain.Bill;
import java.util.List;
import java.util.Optional;
import javax.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {
    Optional<Bill> findAllByOrderId(Integer orderId);

    @Query(
        value = "SELECT SUM(b.total_money) as total, MONTH(o.date_order) as month FROM bill b JOIN order_food o ON b.order_id = o.id WHERE YEAR(o.date_order) = YEAR(CURRENT_TIMESTAMP) GROUP BY MONTH(o.date_order) ",
        nativeQuery = true
    )
    List<Tuple> getTotalMoneyOfTwelve();

    @Query(
        value = " " +
        "	SELECT " +
        "		f.id," +
        "       f.name,	" +
        "	    MONTH(o.date_order) as month, 	" +
        "		SUM(b.total_money) as total												" +
        "	FROM bill b JOIN order_food o ON b.order_id = o.id left join order_food_detail od on od.order_id = o.id left join food f on f.id = od.food_id			" +
        "	WHERE 													" +
        "	    YEAR(o.date_order) = :year and month(o.date_order) = :month        " +
        "	GROUP BY MONTH(o.date_order)		",
        nativeQuery = true
    )
    List<Tuple> getTotalMoneyByMonthAndYear(@Param("month") Integer month, @Param("year") Integer year);
}
