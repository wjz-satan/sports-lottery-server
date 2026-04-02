package com.lottery.service.datasource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lottery.entity.DataSourceConfig;
import com.lottery.entity.League;
import com.lottery.entity.MatchInfo;
import com.lottery.entity.Team;
import com.lottery.repository.DataSourceConfigRepository;
import com.lottery.repository.LeagueRepository;
import com.lottery.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeiSuDataClient implements SportsDataClient {
    private final DataSourceConfigRepository configRepository;
    private final LeagueRepository leagueRepository;
    private final TeamRepository teamRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String getSourceKey() {
        return "leisu";
    }

    @Override
    public List<Team> fetchTeams() throws Exception {
        DataSourceConfig config = getConfig();
        if (config == null || !config.getEnabled()) {
            return new ArrayList<>();
        }

        List<Team> teams = new ArrayList<>();
        try {
            String url = config.getApiUrl() + "/teams?api_key=" + config.getApiKey();
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode data = root.path("result").path("data");

            for (JsonNode node : data) {
                Team team = new Team();
                team.setNameCn(node.path("team_name").asText());
                team.setNameEn(node.path("team_name_en").asText(""));
                team.setStadium(node.path("home_court").asText(""));
                teams.add(team);
            }
        } catch (Exception e) {
            log.error("获取雷速球队数据失败", e);
        }
        return teams;
    }

    @Override
    public List<MatchInfo> fetchMatches(String date) throws Exception {
        DataSourceConfig config = getConfig();
        if (config == null || !config.getEnabled()) {
            return new ArrayList<>();
        }

        List<MatchInfo> matches = new ArrayList<>();
        try {
            String url = config.getApiUrl() + "/matches?api_key=" + config.getApiKey() + "&date=" + date;
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode data = root.path("result").path("data");

            for (JsonNode node : data) {
                MatchInfo match = new MatchInfo();
                match.setMatchCode("LS" + node.path("id").asText());
                match.setLeagueId(1L);
                match.setHomeTeamId(node.path("home_team_id").asLong(1));
                match.setAwayTeamId(node.path("away_team_id").asLong(1));
                String timeStr = node.path("match_time").asText();
                if (timeStr != null && !timeStr.isEmpty()) {
                    match.setMatchTime(LocalDateTime.parse(timeStr, DateTimeFormatter.ISO_DATE_TIME));
                }
                match.setHomeScore(node.path("home_score").asInt(0));
                match.setAwayScore(node.path("away_score").asInt(0));
                match.setStatus(node.path("status").asText("finished"));
                matches.add(match);
            }
        } catch (Exception e) {
            log.error("获取雷速比赛数据失败", e);
        }
        return matches;
    }

    @Override
    public boolean isEnabled() {
        DataSourceConfig config = getConfig();
        return config != null && config.getEnabled();
    }

    private DataSourceConfig getConfig() {
        return configRepository.findBySourceKey("leisu").orElse(null);
    }
}
