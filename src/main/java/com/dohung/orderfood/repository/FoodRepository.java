package com.dohung.orderfood.repository;

import com.dohung.orderfood.domain.Food;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {
    List<Food> findAllByNameAndGroupId(String name, Integer groupId);

    Optional<Food> findAllById(Integer id);
}
