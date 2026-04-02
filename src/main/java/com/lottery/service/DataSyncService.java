package com.lottery.service;

import com.lottery.entity.DataSyncLog;
import com.lottery.repository.DataSyncLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class DataSyncService {
    private final DataSyncLogRepository dataSyncLogRepository;

    public List<DataSyncLog> getSyncLogs() {
        return dataSyncLogRepository.findTop20ByOrderByCreatedAtDesc();
    }

    public DataSyncLog syncData(String syncType) {
        DataSyncLog log = new DataSyncLog();
        log.setDataSource("manual");
        log.setSyncType(syncType);
        log.setSyncStatus("running");
        log.setStartTime(LocalDateTime.now());
        log = dataSyncLogRepository.save(log);

        try {
            // 模拟同步过程
            Thread.sleep(1500);

            Random random = new Random();
            int count = random.nextInt(20) + 1;
            log.setSyncCount(count);
            log.setSyncStatus("success");
            log.setEndTime(LocalDateTime.now());
        } catch (InterruptedException e) {
            log.setSyncStatus("failed");
            log.setErrorMessage(e.getMessage());
            log.setEndTime(LocalDateTime.now());
        }

        return dataSyncLogRepository.save(log);
    }

    public DataSyncLog createSyncLog(String dataSource, String syncType, String status, int count, String error) {
        DataSyncLog log = new DataSyncLog();
        log.setDataSource(dataSource);
        log.setSyncType(syncType);
        log.setSyncStatus(status);
        log.setSyncCount(count);
        log.setErrorMessage(error);
        log.setStartTime(LocalDateTime.now());
        log.setEndTime(LocalDateTime.now());
        return dataSyncLogRepository.save(log);
    }
}
