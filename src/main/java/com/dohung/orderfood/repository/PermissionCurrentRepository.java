package com.dohung.orderfood.repository;

import com.dohung.orderfood.domain.PermissionCurrent;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionCurrentRepository extends JpaRepository<PermissionCurrent, Integer> {
    Optional<PermissionCurrent> findAllByUsername(String username);

    void deleteByUsername(String usernameDeleted);
}
