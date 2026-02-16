package com.turkcell.gameplus.repository;

import com.turkcell.gameplus.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {
    @Query("SELECT n FROM Notification n WHERE n.userId = :userId ORDER BY n.sentAt DESC")
    List<Notification> findByUserIdOrderBySentAtDesc(@Param("userId") String userId);
}

