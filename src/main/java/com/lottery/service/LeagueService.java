package com.lottery.service;

import com.lottery.entity.League;
import com.lottery.repository.LeagueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeagueService {
    private final LeagueRepository leagueRepository;

    public List<League> findAll() {
        return leagueRepository.findByStatusOrderByName(1);
    }

    public List<League> findBySportType(String sportType) {
        return leagueRepository.findBySportTypeOrderByName(sportType);
    }

    public League findById(Long id) {
        return leagueRepository.findById(id).orElse(null);
    }

    public League save(League league) {
        return leagueRepository.save(league);
    }

    public void delete(Long id) {
        League league = findById(id);
        if (league != null) {
            league.setStatus(0);
            leagueRepository.save(league);
        }
    }
}
