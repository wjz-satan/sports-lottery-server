package com.lottery.repository;

import com.lottery.entity.DataSyncLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DataSyncLogRepository extends JpaRepository<DataSyncLog, Long> {
    List<DataSyncLog> findTop20ByOrderByCreatedAtDesc();
    List<DataSyncLog> findBySyncTypeOrderByCreatedAtDesc(String syncType);
}
