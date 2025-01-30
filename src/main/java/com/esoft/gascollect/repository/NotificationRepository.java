package com.esoft.gascollect.repository;

import com.esoft.gascollect.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification,Integer> {
}
