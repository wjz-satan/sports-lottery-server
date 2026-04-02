package com.lottery.controller;

import com.lottery.entity.BettingRecord;
import com.lottery.service.BettingRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/betting")
@RequiredArgsConstructor

public class BettingController {
    private final BettingRecordService bettingRecordService;

    @GetMapping
    public ResponseEntity<List<BettingRecord>> getAll(
            @RequestParam(required = false) String bettingType,
            @RequestParam(required = false) String actualResult,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        if (bettingType != null) {
            return ResponseEntity.ok(bettingRecordService.findByType(bettingType));
        }
        if (actualResult != null) {
            return ResponseEntity.ok(bettingRecordService.findByResult(actualResult));
        }
        if (startDate != null && endDate != null) {
            return ResponseEntity.ok(bettingRecordService.findByDateRange(startDate, endDate));
        }
        return ResponseEntity.ok(bettingRecordService.findAll());
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        return ResponseEntity.ok(bettingRecordService.getStatistics());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BettingRecord> getById(@PathVariable Long id) {
        BettingRecord record = bettingRecordService.findById(id);
        if (record == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(record);
    }

    @PostMapping
    public ResponseEntity<BettingRecord> create(@RequestBody BettingRecord record) {
        return ResponseEntity.ok(bettingRecordService.save(record));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BettingRecord> update(@PathVariable Long id, @RequestBody BettingRecord record) {
        BettingRecord existing = bettingRecordService.findById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        record.setId(id);
        return ResponseEntity.ok(bettingRecordService.save(record));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bettingRecordService.delete(id);
        return ResponseEntity.ok().build();
    }
}
