package com.lottery.repository;

import com.lottery.entity.MatchOdds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MatchOddsRepository extends JpaRepository<MatchOdds, Long> {
    List<MatchOdds> findByMatchIdOrderByRecordTimeDesc(Long matchId);
    List<MatchOdds> findByMatchIdAndOddsTypeOrderByRecordTimeDesc(Long matchId, String oddsType);

    @Query("SELECT m FROM MatchOdds m WHERE m.recordTime BETWEEN :start AND :end ORDER BY m.matchId, m.recordTime DESC")
    List<MatchOdds> findRecentOdds(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
