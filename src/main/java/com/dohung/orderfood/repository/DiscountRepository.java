package com.dohung.orderfood.repository;

import com.dohung.orderfood.domain.Discount;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Integer> {
    List<Discount> findByIsDeletedEquals(Integer isDelete);

    Optional<Discount> findAllById(Integer id);

    List<Discount> findAllByStartDateIsBeforeAndEndDateIsBefore(Date startDate, Date endDate);

    List<Discount> findByStartDateEqualsAndEndDateEqualsAndIsDeletedEqualsAndNameEquals(
        Date startDate,
        Date endDate,
        Integer isDeleted,
        String name
    );

    //    @Query(value = " update food f left join discount d on f.discount_id = d.id   set f.discount_id = null where  d.end_date = curdate() ", nativeQuery = true)
    //    Tuple updateFood();

    //    @Query( value = " SELECT d.id  FROM discount d WHERE d.end_date = :date ",nativeQuery = true)
    //    List<Discount> findAllByEndDate(@Param("date") String date);

    List<Discount> findAllByEndDate(Date date);

    List<Discount> findAllByEndDateGreaterThanEqual(Date date);

    //    List<Discount> findByIsDeletedEqualAndEndDateGreaterThanEqual(Date parse, Integer isDeleted);

    List<Discount> findByIsDeletedEqualsAndEndDateGreaterThanEqual(Integer isDeleted, Date parse);

    List<Discount> findAllByEndDateLessThanEqual(Date parse);

    @Modifying
    @Query(value = "update discount set is_deleted = 1 where id in (:discountIds)", nativeQuery = true)
    void updateDiscountHasEndDateExpireDate(@Param("discountIds") List<Integer> discountIds);

    Optional<Discount> findByIdAndIsDeletedEquals(Integer discountId, int i);
}
