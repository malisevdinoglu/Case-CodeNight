package com.turkcell.gameplus.repository;

import com.turkcell.gameplus.model.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, String> {
}

