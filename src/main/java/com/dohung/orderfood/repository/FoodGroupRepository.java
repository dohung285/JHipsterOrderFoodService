package com.dohung.orderfood.repository;

import com.dohung.orderfood.domain.FoodGroup;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodGroupRepository extends JpaRepository<FoodGroup, Integer> {
    List<FoodGroup> findAllByName(String name);
}
