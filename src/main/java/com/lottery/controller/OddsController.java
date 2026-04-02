package com.lottery.controller;

import com.lottery.entity.MatchOdds;
import com.lottery.service.MatchOddsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/odds")
@RequiredArgsConstructor

public class OddsController {
    private final MatchOddsService matchOddsService;

    @GetMapping
    public ResponseEntity<List<MatchOdds>> getByMatchId(
            @RequestParam Long matchId,
            @RequestParam(required = false) String oddsType) {
        if (oddsType != null) {
            return ResponseEntity.ok(matchOddsService.findByMatchIdAndType(matchId, oddsType));
        }
        return ResponseEntity.ok(matchOddsService.findByMatchId(matchId));
    }

    @GetMapping("/recent")
    public ResponseEntity<List<MatchOdds>> getRecentOdds(
            @RequestParam(required = false, defaultValue = "24") Integer hours) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        return ResponseEntity.ok(matchOddsService.findRecentOdds(since));
    }

    @PostMapping
    public ResponseEntity<MatchOdds> create(@RequestBody MatchOdds odds) {
        return ResponseEntity.ok(matchOddsService.save(odds));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MatchOdds> update(@PathVariable Long id, @RequestBody MatchOdds odds) {
        odds.setId(id);
        return ResponseEntity.ok(matchOddsService.save(odds));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        matchOddsService.delete(id);
        return ResponseEntity.ok().build();
    }
}
