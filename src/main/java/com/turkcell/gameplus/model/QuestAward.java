package com.turkcell.gameplus.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "quest_awards")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestAward {
    @Id
    @Column(name = "award_id")
    private String awardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "user_id", insertable = false, updatable = false)
    private String userId;

    @Column(name = "as_of_date", nullable = false)
    private LocalDate asOfDate;

    @Column(name = "triggered_quests", columnDefinition = "TEXT")
    private String triggeredQuests;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_quest")
    private Quest selectedQuest;

    @Column(name = "selected_quest", insertable = false, updatable = false)
    private String selectedQuestId;

    @Column(name = "reward_points")
    private Integer rewardPoints = 0;

    @Column(name = "suppressed_quests", columnDefinition = "TEXT")
    private String suppressedQuests;

    @Column(name = "timestamp")
    private LocalDateTime timestamp = LocalDateTime.now();
}

