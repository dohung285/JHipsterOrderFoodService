package com.dohung.orderfood.repository;

import com.dohung.orderfood.domain.Food;
import com.dohung.orderfood.domain.FoodDetail;
import com.dohung.orderfood.domain.FoodIdentity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface FoodDetailRepository extends JpaRepository<FoodDetail, FoodIdentity> {
    @Query(value = "SELECT * FROM food_detail WHERE food_id IN (:ids)", nativeQuery = true)
    List<FoodDetail> findAllByIdIn(@Param("ids") List<Integer> ids);

    @Transactional
    @Modifying
    @Query(value = "DELETE from FoodDetail f where f.id.foodId in (:ids)")
    void deleteAllByFoodId(@Param("ids") List<Integer> ids);

    @Query(
        value = "select i.path from food_detail fd join image i on fd.image_id = i.id where fd.food_id=:foodId limit 3",
        nativeQuery = true
    )
    List<String> getAllImageByFoodId(@Param("foodId") Integer foodId);

    void deleteAllById(FoodIdentity ids);
}
