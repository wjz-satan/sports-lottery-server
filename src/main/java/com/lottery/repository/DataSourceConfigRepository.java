package com.lottery.repository;

import com.lottery.entity.DataSourceConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DataSourceConfigRepository extends JpaRepository<DataSourceConfig, Long> {
    List<DataSourceConfig> findByEnabled(Boolean enabled);
    Optional<DataSourceConfig> findBySourceKey(String sourceKey);
}
