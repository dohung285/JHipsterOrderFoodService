package com.dohung.orderfood.repository;

import com.dohung.orderfood.domain.Notification;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    Optional<Notification> findByOrderId(Integer orderId);

    //    List<Notification> findByIsDeletedEqualsAndOrderByCreatedDateDesc(Integer isDeleted);

    List<Notification> findAllByIsDeletedEqualsOrderByCreatedDateDesc(Integer isDeleted);

    List<Notification> findAllByIdIn(List<Integer> ids);

    @Modifying
    @Query("update Notification f set f.isDeleted = 1 where f.id in (:notificationIds)  and f.isDeleted = 0")
    void updateIsDeleted(@Param("notificationIds") List<Integer> notificationIds);
}
