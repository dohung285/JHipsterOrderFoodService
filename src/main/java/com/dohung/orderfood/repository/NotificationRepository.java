package com.dohung.orderfood.repository;

import com.dohung.orderfood.domain.Notification;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    Optional<Notification> findByOrderId(Integer orderId);
}
