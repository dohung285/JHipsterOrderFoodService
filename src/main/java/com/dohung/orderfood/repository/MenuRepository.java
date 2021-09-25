package com.dohung.orderfood.repository;

import com.dohung.orderfood.domain.Menu;
import java.util.List;
import java.util.Optional;
import javax.persistence.Tuple;
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

    @Query(value = "select * from menu m where m.id != 1 and m.parent_id != 1   ", nativeQuery = true)
    List<Menu> getAllMenuWithoutItemHeThongAndSubHeThong();

    @Query(value = "select * from menu m where m.id != 3 and m.parent_id != 3   ", nativeQuery = true)
    List<Menu> getAllMenuWithoutItemMonAnAndSubMonAn();

    @Query(
        value = "select m.name,m.icon,fg.id as foodGroupId, concat('/catalog/',fg.id) as link from menu m left join food_group fg on m.name=fg.name where m.id = 3 or m.parent_id = 3    ",
        nativeQuery = true
    )
    List<Tuple> getAllMenuItemMonAn();

    Optional<Menu> findAllByName(String foodGroupName);
}
