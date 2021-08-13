package com.dohung.orderfood.repository;

import com.dohung.orderfood.domain.Menu;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    @Query(value = "select * from menu m where  m.role_name in (:roles)", nativeQuery = true)
    List<Menu> getAllMenuByRole(@Param("roles") List<String> roles);

    @Query(value = "select * from menu  ", nativeQuery = true) //m where  m.role_name != 'admin'
    List<Menu> getAllMenuNotRole();

    Optional<Menu> findAllByName(String foodGroupName);
}
