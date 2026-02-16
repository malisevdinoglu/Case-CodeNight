package com.turkcell.gameplus.repository;

import com.turkcell.gameplus.model.ActivityEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ActivityEventRepository extends JpaRepository<ActivityEvent, String> {
    List<ActivityEvent> findByUserIdAndDate(String userId, LocalDate date);
    
    List<ActivityEvent> findByUserIdAndDateBetween(String userId, LocalDate start, LocalDate end);
    
    @Query("SELECT ae FROM ActivityEvent ae WHERE ae.userId = :userId AND ae.date <= :date ORDER BY ae.date DESC")
    List<ActivityEvent> findByUserIdAndDateLessThanEqualOrderByDateDesc(@Param("userId") String userId, @Param("date") LocalDate date);
}

