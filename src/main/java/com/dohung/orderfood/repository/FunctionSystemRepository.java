package com.dohung.orderfood.repository;

import com.dohung.orderfood.domain.FunctionSystem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FunctionSystemRepository extends JpaRepository<FunctionSystem, Integer> {
    @Query(
        " select distinct  z.name  from UserPermission x left join ActionSystem  y on x.actionId = y.id left join FunctionSystem z on y.functId = z.id where x.username = :username "
    )
    List<String> getAllFunctionNameByUsername(@Param("username") String username);

    List<FunctionSystem> findByIdIn(List<Integer> functIds);
}
