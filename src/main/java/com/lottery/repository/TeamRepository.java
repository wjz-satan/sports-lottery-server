package com.lottery.repository;

import com.lottery.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findByStatusOrderByNameCn(Integer status);
    List<Team> findByLeagueIdAndStatusOrderByNameCn(Long leagueId, Integer status);
}
