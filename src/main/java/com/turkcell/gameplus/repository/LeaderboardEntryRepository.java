package com.turkcell.gameplus.repository;

import com.turkcell.gameplus.model.LeaderboardEntry;
import com.turkcell.gameplus.model.LeaderboardEntryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaderboardEntryRepository
        extends JpaRepository<LeaderboardEntry, LeaderboardEntryId> {
    List<LeaderboardEntry> findByAsOfDateOrderByTotalPointsDescUserIdAsc(LocalDate asOfDate);

    @Query("SELECT le FROM LeaderboardEntry le WHERE le.asOfDate = :date ORDER BY le.totalPoints DESC, le.userId ASC")
    List<LeaderboardEntry> findByAsOfDateOrdered(@Param("date") LocalDate date);

    LeaderboardEntry findTopByOrderByAsOfDateDesc();
}

