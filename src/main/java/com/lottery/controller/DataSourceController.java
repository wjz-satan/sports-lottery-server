package com.lottery.controller;

import com.lottery.entity.DataSourceConfig;
import com.lottery.entity.DataSyncLog;
import com.lottery.service.DataSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/data-source")
@RequiredArgsConstructor
public class DataSourceController {
    private final DataSyncService dataSyncService;

    @GetMapping("/configs")
    public ResponseEntity<List<DataSourceConfig>> getConfigs() {
        return ResponseEntity.ok(dataSyncService.getAllConfigs());
    }

    @GetMapping("/configs/{sourceKey}")
    public ResponseEntity<DataSourceConfig> getConfig(@PathVariable String sourceKey) {
        DataSourceConfig config = dataSyncService.getConfigByKey(sourceKey);
        if (config == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(config);
    }

    @PostMapping("/configs")
    public ResponseEntity<DataSourceConfig> saveConfig(@RequestBody DataSourceConfig config) {
        return ResponseEntity.ok(dataSyncService.saveConfig(config));
    }

    @PutMapping("/configs/{sourceKey}")
    public ResponseEntity<DataSourceConfig> updateConfig(@PathVariable String sourceKey, @RequestBody DataSourceConfig config) {
        DataSourceConfig existing = dataSyncService.getConfigByKey(sourceKey);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        existing.setApiUrl(config.getApiUrl());
        existing.setApiKey(config.getApiKey());
        existing.setSecretKey(config.getSecretKey());
        existing.setEnabled(config.getEnabled());
        existing.setSyncIntervalMinutes(config.getSyncIntervalMinutes());
        return ResponseEntity.ok(dataSyncService.saveConfig(existing));
    }

    @PostMapping("/sync/{sourceKey}")
    public ResponseEntity<DataSyncLog> syncFromSource(@PathVariable String sourceKey) {
        return ResponseEntity.ok(dataSyncService.syncFromSource(sourceKey));
    }

    @GetMapping("/logs")
    public ResponseEntity<List<DataSyncLog>> getLogs() {
        return ResponseEntity.ok(dataSyncService.getSyncLogs());
    }
}
