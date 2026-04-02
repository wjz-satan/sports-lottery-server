package com.lottery.controller;

import com.lottery.entity.League;
import com.lottery.service.LeagueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/leagues")
@RequiredArgsConstructor

public class LeagueController {
    private final LeagueService leagueService;

    @GetMapping
    public ResponseEntity<List<League>> getAll(
            @RequestParam(required = false) String sportType) {
        if (sportType != null) {
            return ResponseEntity.ok(leagueService.findBySportType(sportType));
        }
        return ResponseEntity.ok(leagueService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<League> getById(@PathVariable Long id) {
        League league = leagueService.findById(id);
        if (league == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(league);
    }

    @PostMapping
    public ResponseEntity<League> create(@RequestBody League league) {
        return ResponseEntity.ok(leagueService.save(league));
    }

    @PutMapping("/{id}")
    public ResponseEntity<League> update(@PathVariable Long id, @RequestBody League league) {
        League existing = leagueService.findById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        league.setId(id);
        return ResponseEntity.ok(leagueService.save(league));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        leagueService.delete(id);
        return ResponseEntity.ok().build();
    }
}
