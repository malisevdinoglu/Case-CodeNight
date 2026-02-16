package com.turkcell.gameplus.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "activity_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityEvent {
    @Id
    @Column(name = "event_id")
    private String eventId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "user_id", insertable = false, updatable = false)
    private String userId;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Column(name = "game_id", insertable = false, updatable = false)
    private String gameId;

    @Column(name = "login_count")
    private Integer loginCount = 0;

    @Column(name = "play_minutes")
    private Integer playMinutes = 0;

    @Column(name = "pvp_wins")
    private Integer pvpWins = 0;

    @Column(name = "coop_minutes")
    private Integer coopMinutes = 0;

    @Column(name = "topup_try")
    private Double topupTry = 0.0;
}

