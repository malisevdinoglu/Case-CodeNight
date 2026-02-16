package com.turkcell.gameplus.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quest {
    @Id
    @Column(name = "quest_id")
    private String questId;

    @Column(name = "quest_name", nullable = false)
    private String questName;

    @Column(name = "quest_type", nullable = false)
    private String questType;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String condition;

    @Column(name = "reward_points", nullable = false)
    private Integer rewardPoints;

    @Column(nullable = false)
    private Integer priority;

    @Column(name = "is_active")
    private Boolean isActive = true;
}

