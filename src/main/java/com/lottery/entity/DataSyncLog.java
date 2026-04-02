package com.lottery.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "data_sync_log")
public class DataSyncLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String dataSource;

    @Column(nullable = false, length = 30)
    private String syncType;

    @Column(length = 20)
    private String syncStatus = "running";

    private Integer syncCount = 0;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (startTime == null) {
            startTime = LocalDateTime.now();
        }
    }
}
