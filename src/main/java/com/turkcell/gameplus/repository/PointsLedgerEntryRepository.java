package com.turkcell.gameplus.repository;

import com.turkcell.gameplus.model.PointsLedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PointsLedgerEntryRepository extends JpaRepository<PointsLedgerEntry, String> {
    @Query("SELECT COALESCE(SUM(p.pointsDelta), 0) FROM PointsLedgerEntry p WHERE p.userId = :userId")
    Integer getTotalPointsByUserId(@Param("userId") String userId);
    
    @Query("SELECT p.userId, SUM(p.pointsDelta) as totalPoints FROM PointsLedgerEntry p GROUP BY p.userId")
    List<Object[]> getTotalPointsForAllUsers();
    
    @Query("SELECT p FROM PointsLedgerEntry p WHERE p.userId = :userId ORDER BY p.createdAt DESC")
    List<PointsLedgerEntry> findByUserIdOrderByCreatedAtDesc(@Param("userId") String userId);
}

