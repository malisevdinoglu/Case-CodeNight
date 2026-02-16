package com.turkcell.gameplus.repository;

import com.turkcell.gameplus.model.UserState;
import com.turkcell.gameplus.model.UserStateId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserStateRepository extends JpaRepository<UserState, UserStateId> {
    Optional<UserState> findByUserIdAndAsOfDate(String userId, LocalDate asOfDate);
    
    @Query("SELECT us FROM UserState us WHERE us.userId = :userId AND us.asOfDate <= :date ORDER BY us.asOfDate DESC")
    List<UserState> findByUserIdAndAsOfDateLessThanEqualOrderByAsOfDateDesc(@Param("userId") String userId, @Param("date") LocalDate date);
}

