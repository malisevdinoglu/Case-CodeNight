package com.turkcell.gameplus.repository;

import com.turkcell.gameplus.model.BadgeAward;
import com.turkcell.gameplus.model.BadgeAwardId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BadgeAwardRepository extends JpaRepository<BadgeAward, BadgeAwardId> {
    List<BadgeAward> findByUserId(String userId);
    
    @Query("SELECT ba FROM BadgeAward ba WHERE ba.userId = :userId AND ba.badgeId = :badgeId")
    Optional<BadgeAward> findByUserIdAndBadgeId(@Param("userId") String userId, @Param("badgeId") String badgeId);
}

