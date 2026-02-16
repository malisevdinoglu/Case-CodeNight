package com.turkcell.gameplus.repository;

import com.turkcell.gameplus.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, String> {
}

