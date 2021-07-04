package com.dohung.orderfood.repository;

import com.dohung.orderfood.domain.Card;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {
    Page<Card> findAllByUsername(Pageable pageable, String username);

    List<Card> findAllByUsername(String username);

    Optional<Card> findAllByUsernameAndFoodId(String username, Integer foodId);
    //    List<Card> findAllByFoodId(Integer foodId);

}
