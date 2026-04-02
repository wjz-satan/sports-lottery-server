package com.lottery.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "match_info")
public class MatchInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 50)
    private String matchCode;

    @Column(nullable = false)
    private Long leagueId;

    private Long seasonId;

    @Column(nullable = false)
    private Long homeTeamId;

    @Column(nullable = false)
    private Long awayTeamId;

    @Column(nullable = false)
    private LocalDateTime matchTime;

    private Integer homeScore;
    private Integer awayScore;
    private Integer homeScoreHalf;
    private Integer awayScoreHalf;

    @Column(length = 20)
    private String status = "pending";

    @Column(columnDefinition = "TEXT")
    private String injuryInfo;

    @Column(columnDefinition = "TEXT")
    private String remark;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
