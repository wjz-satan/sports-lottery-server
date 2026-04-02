package com.lottery.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "betting_record")
public class BettingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long matchId;

    @Column(nullable = false, length = 20)
    private String bettingType;

    @Column(nullable = false, length = 50)
    private String bettingOption;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String bettingContent;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, precision = 8, scale = 3)
    private BigDecimal odds;

    @Column(precision = 12, scale = 2)
    private BigDecimal expectedWin;

    @Column(length = 20)
    private String actualResult = "pending";

    @Column(precision = 12, scale = 2)
    private BigDecimal actualWin;

    private LocalDateTime bettingTime;

    @Column(columnDefinition = "TEXT")
    private String remark;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (bettingTime == null) {
            bettingTime = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
