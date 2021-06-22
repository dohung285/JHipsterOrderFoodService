package com.dohung.orderfood.repository;

import com.dohung.orderfood.domain.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {
    Page<Card> findAllByUsername(Pageable pageable, String username);
    //    List<Card> findAllByFoodId(Integer foodId);

}
