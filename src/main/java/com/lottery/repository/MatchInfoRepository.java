package com.lottery.repository;

import com.lottery.entity.MatchInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatchInfoRepository extends JpaRepository<MatchInfo, Long> {
    List<MatchInfo> findByStatusOrderByMatchTimeDesc(String status);
    List<MatchInfo> findByLeagueIdOrderByMatchTimeDesc(Long leagueId);
    List<MatchInfo> findByMatchTimeBetweenOrderByMatchTimeDesc(LocalDateTime start, LocalDateTime end);

    @Query("SELECT m FROM MatchInfo m WHERE m.homeTeamId = :teamId OR m.awayTeamId = :teamId ORDER BY m.matchTime DESC")
    List<MatchInfo> findByTeamId(@Param("teamId") Long teamId);

    @Query("SELECT m FROM MatchInfo m WHERE (m.homeTeamId = :team1 AND m.awayTeamId = :team2) OR (m.homeTeamId = :team2 AND m.awayTeamId = :team1) ORDER BY m.matchTime DESC")
    List<MatchInfo> findHeadToHead(@Param("team1") Long team1, @Param("team2") Long team2);

    Optional<MatchInfo> findByMatchCode(String matchCode);
}
