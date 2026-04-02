package com.lottery.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")

public class DashboardController {

    @GetMapping("/dashboard/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalMatches", 0);
        stats.put("pendingMatches", 0);
        stats.put("finishedMatches", 0);
        stats.put("totalTeams", 0);
        stats.put("totalLeagues", 0);
        stats.put("winRate", "0%");
        stats.put("totalBetAmount", 0);
        stats.put("netProfit", 0);
        return ResponseEntity.ok(stats);
    }
}
