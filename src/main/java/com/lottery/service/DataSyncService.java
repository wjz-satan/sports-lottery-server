package com.lottery.service;

import com.lottery.entity.*;
import com.lottery.repository.*;
import com.lottery.service.datasource.SportsDataClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataSyncService {
    private final DataSourceConfigRepository configRepository;
    private final DataSyncLogRepository syncLogRepository;
    private final LeagueRepository leagueRepository;
    private final TeamRepository teamRepository;
    private final MatchInfoRepository matchInfoRepository;
    private final MatchOddsRepository matchOddsRepository;
    private final Map<String, SportsDataClient> dataClients;

    public List<DataSourceConfig> getAllConfigs() {
        return configRepository.findAll();
    }

    public DataSourceConfig getConfigByKey(String sourceKey) {
        return configRepository.findBySourceKey(sourceKey).orElse(null);
    }

    @Transactional
    public DataSourceConfig saveConfig(DataSourceConfig config) {
        config.setSourceKey(config.getSourceKey());
        return configRepository.save(config);
    }

    @Transactional
    public DataSyncLog syncFromSource(String sourceKey) {
        DataSyncLog syncLog = new DataSyncLog();
        syncLog.setDataSource(sourceKey);
        syncLog.setSyncStatus("running");
        syncLog.setStartTime(LocalDateTime.now());
        syncLog = syncLogRepository.save(syncLog);

        try {
            SportsDataClient client = dataClients.get(sourceKey);
            if (client == null) {
                throw new Exception("数据源不存在: " + sourceKey);
            }

            if (!client.isEnabled()) {
                throw new Exception("数据源未启用: " + sourceKey);
            }

            int totalCount = 0;

            // 同步球队
            syncLog.setSyncType("team");
            List<Team> teams = client.fetchTeams();
            for (Team team : teams) {
                syncTeam(team);
                totalCount++;
            }

            // 同步今日比赛
            syncLog.setSyncType("match");
            String today = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
            List<MatchInfo> matches = client.fetchMatches(today);
            for (MatchInfo match : matches) {
                syncMatch(match);
                totalCount++;
            }

            syncLog.setSyncCount(totalCount);
            syncLog.setSyncStatus("success");
            syncLog.setEndTime(LocalDateTime.now());
        } catch (Exception e) {
            log.error("数据同步失败: " + sourceKey, e);
            syncLog.setSyncStatus("failed");
            syncLog.setErrorMessage(e.getMessage());
            syncLog.setEndTime(LocalDateTime.now());
        }

        return syncLogRepository.save(syncLog);
    }

    private void syncTeam(Team team) {
        List<Team> existing = teamRepository.findByStatusOrderByNameCn(1);
        boolean exists = existing.stream()
                .anyMatch(t -> t.getNameCn().equals(team.getNameCn()));
        if (!exists) {
            team.setStatus(1);
            teamRepository.save(team);
        }
    }

    private void syncMatch(MatchInfo match) {
        if (match.getMatchCode() == null) return;
        java.util.Optional<MatchInfo> existing = matchInfoRepository.findByMatchCode(match.getMatchCode());
        if (existing.isEmpty()) {
            match.setStatus("pending");
            matchInfoRepository.save(match);
        }
    }

    public List<DataSyncLog> getSyncLogs() {
        return syncLogRepository.findTop20ByOrderByCreatedAtDesc();
    }

    @Scheduled(cron = "0 0 6,12,18 * * ?")
    public void scheduledSync() {
        List<DataSourceConfig> enabledConfigs = configRepository.findByEnabled(true);
        for (DataSourceConfig config : enabledConfigs) {
            try {
                log.info("定时同步开始: " + config.getSourceName());
                syncFromSource(config.getSourceKey());
                log.info("定时同步完成: " + config.getSourceName());
            } catch (Exception e) {
                log.error("定时同步失败: " + config.getSourceKey(), e);
            }
        }
    }

    @Transactional
    public void initDefaultConfigs() {
        if (configRepository.count() == 0) {
            DataSourceConfig qiutan = new DataSourceConfig();
            qiutan.setSourceKey("qiutan");
            qiutan.setSourceName("球探数据");
            qiutan.setDescription("主流体育数据源，提供联赛、球队、比赛、赔率数据");
            qiutan.setEnabled(false);
            configRepository.save(qiutan);

            DataSourceConfig leisu = new DataSourceConfig();
            leisu.setSourceKey("leisu");
            leisu.setSourceName("雷速体育");
            leisu.setDescription("专业赛事数据，提供实时比分、赔率数据");
            leisu.setEnabled(false);
            configRepository.save(leisu);

            DataSourceConfig dongqiu = new DataSourceConfig();
            dongqiu.setSourceKey("dongqiu");
            dongqiu.setSourceName("懂球帝");
            dongqiu.setDescription("足球数据源，提供比赛分析数据");
            dongqiu.setEnabled(false);
            configRepository.save(dongqiu);
        }
    }
}
