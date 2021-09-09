package com.dohung.orderfood.repository;

import com.dohung.orderfood.domain.Food;
import com.dohung.orderfood.web.rest.response.FoodByCatalogResponseDto;
import java.util.List;
import java.util.Optional;
import javax.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {
    List<Food> findAllByNameAndGroupIdAndIsDeletedEquals(String name, Integer groupId, Integer isDeleted);

    List<Food> findAllByNameAndGroupId(String name, Integer groupId);

    Optional<Food> findAllById(Integer id);

    @Query(
        value = "select new com.dohung.orderfood.web.rest.response.FoodByCatalogResponseDto( f.id, f.name , f.price,f.description,d.percent, i.path ) from Food f left join Discount d on d.id=f.discountId left join Image i  on f.imageId = i.id where f.id =:foodId and f.isDeleted = 0 "
    )
    Optional<FoodByCatalogResponseDto> getAllById(@Param("foodId") Integer foodId);

    @Query(
        value = "select new com.dohung.orderfood.web.rest.response.FoodByCatalogResponseDto( f.id, f.name , f.price,f.description,d.percent, i.path ) from Food f left join Discount d on d.id=f.discountId left join Image i  on f.imageId = i.id where f.discountId is null and f.isDeleted = 0 "
    )
    List<FoodByCatalogResponseDto> getAllFoodForDiscount();

    @Query(
        value = "select new com.dohung.orderfood.web.rest.response.FoodByCatalogResponseDto( f.id, f.name , f.price,f.description,d.percent, i.path ) from Food f left join Discount d on d.id=f.discountId left join Image i  on f.imageId = i.id where f.discountId is not null  and f.isDeleted = 0 "
    )
    List<FoodByCatalogResponseDto> getAllFoodHadDiscount();

    //    @Query(value = "select new com.dohung.orderfood.web.rest.response.FoodByCatalogResponseDto( f.id,f.name,f.price,f.description,f.discount_id, i.path ) from food f inner join image i on f.image_id = i.id where f.group_id = :foodGroupId ", nativeQuery = true)
    //    Page<Food> findAllByGroupId(Pageable paging, @Param("foodGroupId") Integer foodGroupId);

    @Query(
        value = "select  f.id, f.name , f.price,f.description,f.discountId, i.path  from Food f join Image i  on f.imageId = i.id where f.groupId =:foodGroupId  and f.isDeleted = 0 "
    )
    Page<Object[]> findAllByGroupIdX(@Param("foodGroupId") Integer foodGroupId, Pageable pageable);

    @Query(
        value = "select new com.dohung.orderfood.web.rest.response.FoodByCatalogResponseDto( f.id, f.name , f.price,f.description,f.discountId,f.isDeleted,i.path ) from Food f left join Image i  on f.imageId = i.id where f.groupId =:foodGroupId "
    )
    List<FoodByCatalogResponseDto> getAllByGroupId(@Param("foodGroupId") Integer foodGroupId);

    //    Optional<Food> findAllByGroupId(Integer foodGroupId); new com.dohung.orderfood.web.rest.response.FoodByCatalogResponseDto

    @Query(
        value = "select f.name , count(od.food_id) as number from food f left join order_food_detail od on f.id = od.food_id left join order_food o on o.id = od.order_id  where ( day(o.date_order) = DAY(CURDATE())  and f.is_deleted = 0 ) group by f.id  ",
        nativeQuery = true
    )
    List<Tuple> getCountBuyOfAllFood();

    //    List<Food> findAllByGroupIdAndIsDeletedEquals0(Integer foodGroupId);

    @Query("select f from Food f where f.discountId in (:discountIds)  and f.isDeleted = 0")
    List<Food> getAllFoodsWithDiscountIds(@Param("discountIds") List<Integer> discountIds);

    @Modifying
    @Query("update Food f set f.discountId = null where f.id in (:foodIds)  and f.isDeleted = 0")
    void updateDiscountIdInFood(@Param("foodIds") List<Integer> foodIds);

    @Modifying
    @Query("update Food f set f.isDeleted = 1 where f.id in (:foodIds)  and f.isDeleted = 0")
    void updateIsDeleted(@Param("foodIds") List<Integer> foodIds);

    //    List<Food> findAllByNameLikeAndGroupIdAndIsDeletedEquals0(String foodName, Integer foodGroupId);

    @Query(
        "" +
        "	SELECT 	" +
        "	    f.id ,	" +
        "	    f.name ,	" +
        "	    f.price ,	" +
        "	    f.description ,	" +
        "	    f.discountId ,	" +
        "	    i.path 	" +
        "	FROM	" +
        "	    Food f	" +
        "	        LEFT OUTER JOIN	" +
        "	    Image i ON (f.imageId = i.id)	" +
        "	WHERE	" +
        "	    f.groupId = :foodGroupId	" +
        "	        AND (f.name LIKE %:foodName%)  and f.isDeleted = 0	" +
        ""
    )
    List<Tuple> getAllByNameContainingAndGroupId(@Param("foodName") String foodName, @Param("foodGroupId") Integer foodGroupId);

    @Query(" select f.id,f.name,i.path, f.price from Food f left join Image i on f.imageId = i.id  where f.isDeleted = 0 ")
    List<Tuple> getAllNoPaging();

    List<Food> findAllByIdIn(List<Integer> ids);

    List<Food> findAllByGroupId(Integer foodGroupId);

    Optional<Food> findAllByName(String name);

    List<Food> findAllByNameAndNameNot(String name, String currentName);
}
