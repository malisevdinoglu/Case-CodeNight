package com.turkcell.gameplus.repository;

import com.turkcell.gameplus.model.Quest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestRepository extends JpaRepository<Quest, String> {
    List<Quest> findByIsActiveTrue();
}

