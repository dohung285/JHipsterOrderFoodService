package com.dohung.orderfood.repository;

import com.dohung.orderfood.domain.Card;
import com.dohung.orderfood.web.rest.response.FoodCardResponseDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {
    //    @Query(value = "select new com.dohung.orderfood.web.rest.response.FoodCardResponseDto( c.id,f.id,f.name,f.price,c.amount , i.path , d.percent ) from Card c join Food f on c.foodId = f.id left join Image i on f.imageId = i.id left join Discount d on d.id = f.discountId where c.username =:username  ")
    //    Page<FoodCardResponseDto> findAllByUsername(Pageable pageable, @Param("username") String username);

    @Query(
        value = "select new com.dohung.orderfood.web.rest.response.FoodCardResponseDto( c.id,f.id,f.name,f.price,c.amount , i.path , d.percent ) from Card c join Food f on c.foodId = f.id left join Image i on f.imageId = i.id left join Discount d on d.id = f.discountId where c.username =:username  "
    )
    List<FoodCardResponseDto> findAllByUsernameQuery(@Param("username") String username);

    List<Card> findAllByUsername(String username);

    Optional<Card> findAllByUsernameAndFoodId(String username, Integer foodId);

    Optional<Card> findAllById(Integer cardId);

    //    List<Card> findAllByFoodId(Integer foodId);

    @Query(value = "delete from card where id in (:ids) ", nativeQuery = true)
    void deleteCards(@Param("ids") List<Integer> ids);

    List<Card> findAllByIdIn(List<Integer> ids);
}
