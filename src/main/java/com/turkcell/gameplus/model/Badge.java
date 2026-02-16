package com.turkcell.gameplus.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "badges")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Badge {
    @Id
    @Column(name = "badge_id")
    private String badgeId;

    @Column(name = "badge_name", nullable = false)
    private String badgeName;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String condition;

    @Column(nullable = false)
    private Integer level;
}

