package com.lottery.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "match_odds")
public class MatchOdds {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long matchId;

    @Column(nullable = false, length = 30)
    private String oddsType;

    @Column(length = 50)
    private String handicap;

    private BigDecimal homeOdds;
    private BigDecimal drawOdds;
    private BigDecimal awayOdds;
    private BigDecimal overOdds;
    private BigDecimal underOdds;

    @Column(length = 50)
    private String dataSource;

    private LocalDateTime recordTime;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (recordTime == null) {
            recordTime = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
