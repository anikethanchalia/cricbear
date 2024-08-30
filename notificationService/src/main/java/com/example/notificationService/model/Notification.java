package com.example.notificationService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int nid;

    @Column
    private int uid;


    @Column
    private String reminderType;

    @Column
    private String message;

    @Column
    private LocalDateTime sentAt;


    // Constructors
    public Notification(Integer uid, String reminderType, String message, LocalDateTime now){
        this.uid = uid;
        this.reminderType = reminderType;
        this.message = message;
    }

}


