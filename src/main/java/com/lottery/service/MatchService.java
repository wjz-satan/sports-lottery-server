package com.lottery.service;

import com.lottery.entity.MatchInfo;
import com.lottery.repository.MatchInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchInfoRepository matchInfoRepository;

    public List<MatchInfo> findAll() {
        return matchInfoRepository.findAll();
    }

    public List<MatchInfo> findByStatus(String status) {
        return matchInfoRepository.findByStatusOrderByMatchTimeDesc(status);
    }

    public List<MatchInfo> findByLeagueId(Long leagueId) {
        return matchInfoRepository.findByLeagueIdOrderByMatchTimeDesc(leagueId);
    }

    public List<MatchInfo> findByDateRange(LocalDateTime start, LocalDateTime end) {
        return matchInfoRepository.findByMatchTimeBetweenOrderByMatchTimeDesc(start, end);
    }

    public List<MatchInfo> findByTeamId(Long teamId) {
        return matchInfoRepository.findByTeamId(teamId);
    }

    public List<MatchInfo> findHeadToHead(Long team1, Long team2) {
        return matchInfoRepository.findHeadToHead(team1, team2);
    }

    public MatchInfo findById(Long id) {
        return matchInfoRepository.findById(id).orElse(null);
    }

    public MatchInfo save(MatchInfo matchInfo) {
        return matchInfoRepository.save(matchInfo);
    }

    public void delete(Long id) {
        MatchInfo match = findById(id);
        if (match != null) {
            matchInfoRepository.delete(match);
        }
    }
}
