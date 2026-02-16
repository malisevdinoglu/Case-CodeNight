package com.turkcell.gameplus.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "badge_awards")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(BadgeAwardId.class)
public class BadgeAward {
    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Id
    @Column(name = "badge_id", nullable = false)
    private String badgeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id", insertable = false, updatable = false)
    private Badge badge;

    @Column(name = "awarded_at")
    private LocalDateTime awardedAt = LocalDateTime.now();
}

