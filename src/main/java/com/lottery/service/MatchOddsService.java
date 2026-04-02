package com.lottery.service;

import com.lottery.entity.MatchOdds;
import com.lottery.repository.MatchOddsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchOddsService {
    private final MatchOddsRepository matchOddsRepository;

    public List<MatchOdds> findByMatchId(Long matchId) {
        return matchOddsRepository.findByMatchIdOrderByRecordTimeDesc(matchId);
    }

    public List<MatchOdds> findByMatchIdAndType(Long matchId, String oddsType) {
        return matchOddsRepository.findByMatchIdAndOddsTypeOrderByRecordTimeDesc(matchId, oddsType);
    }

    public List<MatchOdds> findRecentOdds(LocalDateTime since) {
        return matchOddsRepository.findRecentOdds(since, LocalDateTime.now());
    }

    public MatchOdds save(MatchOdds matchOdds) {
        return matchOddsRepository.save(matchOdds);
    }

    public void delete(Long id) {
        MatchOdds odds = matchOddsRepository.findById(id).orElse(null);
        if (odds != null) {
            matchOddsRepository.delete(odds);
        }
    }
}
