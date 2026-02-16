package com.turkcell.gameplus.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "points_ledger")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointsLedgerEntry {
    @Id
    @Column(name = "ledger_id")
    private String ledgerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "user_id", insertable = false, updatable = false)
    private String userId;

    @Column(name = "points_delta", nullable = false)
    private Integer pointsDelta;

    @Column(nullable = false)
    private String source;

    @Column(name = "source_ref")
    private String sourceRef;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}

