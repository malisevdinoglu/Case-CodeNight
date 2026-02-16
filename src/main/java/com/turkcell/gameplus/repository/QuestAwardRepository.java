package com.turkcell.gameplus.repository;

import com.turkcell.gameplus.model.QuestAward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface QuestAwardRepository extends JpaRepository<QuestAward, String> {
    List<QuestAward> findByUserIdAndAsOfDate(String userId, LocalDate asOfDate);
    
    @Query("SELECT qa FROM QuestAward qa WHERE qa.userId = :userId ORDER BY qa.asOfDate DESC")
    List<QuestAward> findByUserIdOrderByAsOfDateDesc(@Param("userId") String userId);
}

