package com.lottery.service;

import com.lottery.entity.BettingRecord;
import com.lottery.repository.BettingRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BettingRecordService {
    private final BettingRecordRepository bettingRecordRepository;

    public List<BettingRecord> findAll() {
        return bettingRecordRepository.findAll();
    }

    public List<BettingRecord> findByType(String bettingType) {
        return bettingRecordRepository.findByBettingTypeOrderByBettingTimeDesc(bettingType);
    }

    public List<BettingRecord> findByResult(String actualResult) {
        return bettingRecordRepository.findByActualResultOrderByBettingTimeDesc(actualResult);
    }

    public List<BettingRecord> findByDateRange(LocalDateTime start, LocalDateTime end) {
        return bettingRecordRepository.findByBettingTimeBetweenOrderByBettingTimeDesc(start, end);
    }

    public BettingRecord findById(Long id) {
        return bettingRecordRepository.findById(id).orElse(null);
    }

    public BettingRecord save(BettingRecord record) {
        // 计算预计中奖金额
        if (record.getExpectedWin() == null && record.getAmount() != null && record.getOdds() != null) {
            record.setExpectedWin(record.getAmount().multiply(record.getOdds()));
        }
        return bettingRecordRepository.save(record);
    }

    public void delete(Long id) {
        BettingRecord record = findById(id);
        if (record != null) {
            bettingRecordRepository.delete(record);
        }
    }

    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();

        BigDecimal totalAmount = bettingRecordRepository.sumTotalAmount();
        BigDecimal winAmount = bettingRecordRepository.sumAmountByResult("win");
        BigDecimal loseAmount = bettingRecordRepository.sumAmountByResult("lose");
        BigDecimal actualWin = bettingRecordRepository.sumActualWin();

        long totalCount = bettingRecordRepository.count();
        long winCount = bettingRecordRepository.findByActualResultOrderByBettingTimeDesc("win").size();
        long pendingCount = bettingRecordRepository.findByActualResultOrderByBettingTimeDesc("pending").size();

        stats.put("totalAmount", totalAmount != null ? totalAmount : BigDecimal.ZERO);
        stats.put("winAmount", winAmount != null ? winAmount : BigDecimal.ZERO);
        stats.put("loseAmount", loseAmount != null ? loseAmount : BigDecimal.ZERO);
        stats.put("actualWin", actualWin != null ? actualWin : BigDecimal.ZERO);
        stats.put("totalCount", totalCount);
        stats.put("winCount", winCount);
        stats.put("pendingCount", pendingCount);

        // 计算胜率和回报率
        if (totalCount > 0) {
            double winRate = (double) winCount / totalCount * 100;
            stats.put("winRate", String.format("%.2f%%", winRate));
        } else {
            stats.put("winRate", "0%");
        }

        BigDecimal netProfit = (actualWin != null ? actualWin : BigDecimal.ZERO)
                .subtract(totalAmount != null ? totalAmount : BigDecimal.ZERO);
        stats.put("netProfit", netProfit);

        if (totalAmount != null && totalAmount.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal roi = netProfit.divide(totalAmount, 4, java.math.RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            stats.put("roi", String.format("%.2f%%", roi));
        } else {
            stats.put("roi", "0%");
        }

        return stats;
    }
}
