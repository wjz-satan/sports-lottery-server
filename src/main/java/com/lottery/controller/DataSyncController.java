package com.lottery.controller;

import com.lottery.entity.DataSyncLog;
import com.lottery.service.DataSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/data-sync")
@RequiredArgsConstructor
public class DataSyncController {
    private final DataSyncService dataSyncService;

    @GetMapping("/logs")
    public ResponseEntity<List<DataSyncLog>> getSyncLogs() {
        return ResponseEntity.ok(dataSyncService.getSyncLogs());
    }

    @PostMapping("/sync")
    public ResponseEntity<DataSyncLog> syncData(@RequestBody Map<String, String> request) {
        String syncType = request.get("syncType");
        return ResponseEntity.ok(dataSyncService.syncFromSource(syncType));
    }

    @GetMapping("/config")
    public ResponseEntity<Map<String, Object>> getConfig() {
        return ResponseEntity.ok(Map.of(
            "qiutan", Map.of("enabled", false, "url", "", "name", "球探数据"),
            "leisu", Map.of("enabled", false, "url", "", "name", "雷速体育"),
            "dongqiu", Map.of("enabled", false, "url", "", "name", "懂球帝")
        ));
    }

    @PostMapping("/config")
    public ResponseEntity<Void> saveConfig(@RequestBody Map<String, Map<String, Object>> config) {
        // 配置保存逻辑，后续可扩展到数据库
        return ResponseEntity.ok().build();
    }
}
