package com.turkcell.gameplus.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "games")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {
    @Id
    @Column(name = "game_id")
    private String gameId;

    @Column(name = "game_name", nullable = false)
    private String gameName;

    private String genre;
}

