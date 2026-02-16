package com.turkcell.gameplus.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "leaderboard")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(LeaderboardEntryId.class)
public class LeaderboardEntry {
    @Id
    @Column(nullable = false)
    private Integer rank;

    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "total_points", nullable = false)
    private Integer totalPoints;

    @Id
    @Column(name = "as_of_date", nullable = false)
    private LocalDate asOfDate;
}

