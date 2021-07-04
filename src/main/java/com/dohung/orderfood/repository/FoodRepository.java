package com.dohung.orderfood.repository;

import com.dohung.orderfood.domain.Food;
import com.dohung.orderfood.web.rest.response.FoodByCatalogResponseDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {
    List<Food> findAllByNameAndGroupId(String name, Integer groupId);

    Optional<Food> findAllById(Integer id);

    @Query(
        value = "select new com.dohung.orderfood.web.rest.response.FoodByCatalogResponseDto( f.id, f.name , f.price,f.description,f.discountId, i.path ) from Food f join Image i  on f.imageId = i.id where f.id =:foodId "
    )
    Optional<FoodByCatalogResponseDto> getAllById(@Param("foodId") Integer foodId);

    //    @Query(value = "select new com.dohung.orderfood.web.rest.response.FoodByCatalogResponseDto( f.id,f.name,f.price,f.description,f.discount_id, i.path ) from food f inner join image i on f.image_id = i.id where f.group_id = :foodGroupId ", nativeQuery = true)
    //    Page<Food> findAllByGroupId(Pageable paging, @Param("foodGroupId") Integer foodGroupId);

    @Query(
        value = "select  f.id, f.name , f.price,f.description,f.discountId, i.path  from Food f join Image i  on f.imageId = i.id where f.groupId =:foodGroupId "
    )
    Page<Object[]> findAllByGroupIdX(@Param("foodGroupId") Integer foodGroupId, Pageable pageable);

    @Query(
        value = "select new com.dohung.orderfood.web.rest.response.FoodByCatalogResponseDto( f.id, f.name , f.price,f.description,f.discountId, i.path ) from Food f join Image i  on f.imageId = i.id where f.groupId =:foodGroupId "
    )
    Page<FoodByCatalogResponseDto> findAllByGroupId(@Param("foodGroupId") Integer foodGroupId, Pageable paging);
    //    Optional<Food> findAllByGroupId(Integer foodGroupId); new com.dohung.orderfood.web.rest.response.FoodByCatalogResponseDto
}
