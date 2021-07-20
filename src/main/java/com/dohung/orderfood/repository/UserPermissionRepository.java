package com.dohung.orderfood.repository;

import com.dohung.orderfood.domain.FunctionSystem;
import com.dohung.orderfood.domain.UserPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPermissionRepository extends JpaRepository<UserPermission, Integer> {
    void deleteByUsername(String username);
}
