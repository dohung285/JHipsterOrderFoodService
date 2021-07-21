package com.dohung.orderfood.repository;

import com.dohung.orderfood.domain.FunctionSystem;
import com.dohung.orderfood.domain.UserPermission;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPermissionRepository extends JpaRepository<UserPermission, Integer> {
    void deleteByUsername(String username);

    @Query(
        " SELECT u.id FROM UserPermission u join ActionSystem a  on u.actionId = a.id join FunctionSystem f  on f.id = a.functId where u.username = :username and f.pathName = :pathName  and a.action = :action "
    )
    Optional<UserPermission> checkExistPermission(
        @Param("username") String username,
        @Param("pathName") String pathName,
        @Param("action") String action
    );
}
