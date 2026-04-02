package com.lottery.repository;

import com.lottery.entity.BettingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BettingRecordRepository extends JpaRepository<BettingRecord, Long> {
    List<BettingRecord> findByBettingTypeOrderByBettingTimeDesc(String bettingType);
    List<BettingRecord> findByActualResultOrderByBettingTimeDesc(String actualResult);
    List<BettingRecord> findByBettingTimeBetweenOrderByBettingTimeDesc(LocalDateTime start, LocalDateTime end);

    @Query("SELECT SUM(br.amount) FROM BettingRecord br")
    BigDecimal sumTotalAmount();

    @Query("SELECT SUM(br.amount) FROM BettingRecord br WHERE br.actualResult = :result")
    BigDecimal sumAmountByResult(@Param("result") String result);

    @Query("SELECT SUM(br.actualWin) FROM BettingRecord br WHERE br.actualResult = 'win'")
    BigDecimal sumActualWin();
}
