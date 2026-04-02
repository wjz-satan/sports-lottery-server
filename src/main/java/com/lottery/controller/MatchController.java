package com.lottery.controller;

import com.lottery.entity.MatchInfo;
import com.lottery.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor

public class MatchController {
    private final MatchService matchService;

    @GetMapping
    public ResponseEntity<List<MatchInfo>> getAll(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long leagueId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        if (status != null) {
            return ResponseEntity.ok(matchService.findByStatus(status));
        }
        if (leagueId != null) {
            return ResponseEntity.ok(matchService.findByLeagueId(leagueId));
        }
        if (startDate != null && endDate != null) {
            return ResponseEntity.ok(matchService.findByDateRange(startDate, endDate));
        }
        return ResponseEntity.ok(matchService.findAll());
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<MatchInfo>> getByTeamId(@PathVariable Long teamId) {
        return ResponseEntity.ok(matchService.findByTeamId(teamId));
    }

    @GetMapping("/head-to-head")
    public ResponseEntity<List<MatchInfo>> getHeadToHead(
            @RequestParam Long team1, @RequestParam Long team2) {
        return ResponseEntity.ok(matchService.findHeadToHead(team1, team2));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchInfo> getById(@PathVariable Long id) {
        MatchInfo match = matchService.findById(id);
        if (match == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(match);
    }

    @PostMapping
    public ResponseEntity<MatchInfo> create(@RequestBody MatchInfo match) {
        return ResponseEntity.ok(matchService.save(match));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MatchInfo> update(@PathVariable Long id, @RequestBody MatchInfo match) {
        MatchInfo existing = matchService.findById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        match.setId(id);
        return ResponseEntity.ok(matchService.save(match));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        matchService.delete(id);
        return ResponseEntity.ok().build();
    }
}
