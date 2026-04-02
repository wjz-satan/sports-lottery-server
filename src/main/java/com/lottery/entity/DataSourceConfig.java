package com.lottery.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "data_source_config")
public class DataSourceConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String sourceKey;

    @Column(nullable = false, length = 100)
    private String sourceName;

    @Column(length = 500)
    private String apiUrl;

    @Column(length = 200)
    private String apiKey;

    @Column(length = 200)
    private String secretKey;

    @Column(nullable = false)
    private Boolean enabled = false;

    @Column(length = 500)
    private String description;

    private Integer syncIntervalMinutes = 360;

    @Column(nullable = false)
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
