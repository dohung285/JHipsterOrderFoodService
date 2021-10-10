package com.dohung.orderfood.repository;

import com.dohung.orderfood.domain.ActionSystem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionSystemRepository extends JpaRepository<ActionSystem, String> {
    @Query(value = "select * from action_system a where a.action != 'PARENT-ACTION' ", nativeQuery = true)
    List<ActionSystem> getAllIgnoreCaseActionIsParent();
}
