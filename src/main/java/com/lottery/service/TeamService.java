package com.lottery.service;

import com.lottery.entity.Team;
import com.lottery.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;

    public List<Team> findAll() {
        return teamRepository.findByStatusOrderByNameCn(1);
    }

    public List<Team> findByLeagueId(Long leagueId) {
        return teamRepository.findByLeagueIdAndStatusOrderByNameCn(leagueId, 1);
    }

    public Team findById(Long id) {
        return teamRepository.findById(id).orElse(null);
    }

    public Team save(Team team) {
        return teamRepository.save(team);
    }

    public void delete(Long id) {
        Team team = findById(id);
        if (team != null) {
            team.setStatus(0);
            teamRepository.save(team);
        }
    }
}
