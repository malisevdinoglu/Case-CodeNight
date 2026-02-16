package com.turkcell.gameplus.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "user_states")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UserStateId.class)
public class UserState {
    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Id
    @Column(name = "as_of_date", nullable = false)
    private LocalDate asOfDate;

    @Column(name = "login_count_today")
    private Integer loginCountToday = 0;

    @Column(name = "play_minutes_today")
    private Integer playMinutesToday = 0;

    @Column(name = "pvp_wins_today")
    private Integer pvpWinsToday = 0;

    @Column(name = "coop_minutes_today")
    private Integer coopMinutesToday = 0;

    @Column(name = "topup_try_today")
    private Double topupTryToday = 0.0;

    @Column(name = "play_minutes_7d")
    private Integer playMinutes7d = 0;

    @Column(name = "topup_try_7d")
    private Double topupTry7d = 0.0;

    @Column(name = "logins_7d")
    private Integer logins7d = 0;

    @Column(name = "login_streak_days")
    private Integer loginStreakDays = 0;
}

