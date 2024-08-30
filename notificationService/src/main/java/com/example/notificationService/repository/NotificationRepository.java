package com.example.notificationService.repository;



import com.example.notificationService.model.Notification;
import com.example.notificationService.repository.NotificationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

}

