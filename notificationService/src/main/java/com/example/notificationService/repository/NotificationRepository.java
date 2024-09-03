package com.example.notificationService.repository;



import com.example.notificationService.model.Notification;
import com.example.notificationService.repository.NotificationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    @Query(value = "SELECT n FROM Notification as n WHERE n.sentAt = :date")
    List<Notification> findWithCurrentDate(Date date);
}

