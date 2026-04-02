package com.lottery.config;

import com.lottery.service.DataSyncService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulingConfig {
    private final DataSyncService dataSyncService;

    @PostConstruct
    public void init() {
        dataSyncService.initDefaultConfigs();
    }
}
