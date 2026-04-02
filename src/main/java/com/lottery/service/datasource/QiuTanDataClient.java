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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class QiuTanDataClient implements SportsDataClient {
    private final DataSourceConfigRepository configRepository;
    private final LeagueRepository leagueRepository;
    private final TeamRepository teamRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String getSourceKey() {
        return "qiutan";
    }

    @Override
    public List<Team> fetchTeams() throws Exception {
        DataSourceConfig config = getConfig();
        if (config == null || !config.getEnabled()) {
            return new ArrayList<>();
        }

        List<Team> teams = new ArrayList<>();
        try {
            // 球探数据API格式（示例）
            String url = config.getApiUrl() + "/team/list?key=" + config.getApiKey();
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode data = root.path("data");

            for (JsonNode node : data) {
                Team team = new Team();
                team.setNameCn(node.path("name_cn").asText(node.path("name").asText()));
                team.setNameEn(node.path("name_en").asText(""));
                team.setLeagueId(getLeagueIdByName(node.path("league").asText()));
                team.setStadium(node.path("stadium").asText(""));
                team.setFoundedYear(node.path("founded_year").asInt(0));
                teams.add(team);
            }
        } catch (Exception e) {
            log.error("获取球探球队数据失败", e);
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
            String url = config.getApiUrl() + "/match/list?key=" + config.getApiKey() + "&date=" + date;
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode data = root.path("data");

            for (JsonNode node : data) {
                MatchInfo match = new MatchInfo();
                match.setMatchCode(node.path("match_id").asText());
                match.setLeagueId(getLeagueIdByName(node.path("league").asText()));
                match.setHomeTeamId(getTeamIdByName(node.path("home_team").asText()));
                match.setAwayTeamId(getTeamIdByName(node.path("away_team").asText()));
                match.setMatchTime(LocalDateTime.parse(node.path("match_time").asText(), DateTimeFormatter.ISO_DATE_TIME));
                match.setHomeScore(node.path("home_score").asInt(0));
                match.setAwayScore(node.path("away_score").asInt(0));
                match.setStatus("finished");
                matches.add(match);
            }
        } catch (Exception e) {
            log.error("获取球探比赛数据失败", e);
        }
        return matches;
    }

    @Override
    public boolean isEnabled() {
        DataSourceConfig config = getConfig();
        return config != null && config.getEnabled();
    }

    private DataSourceConfig getConfig() {
        return configRepository.findBySourceKey("qiutan").orElse(null);
    }

    private Long getLeagueIdByName(String name) {
        Optional<League> league = leagueRepository.findAll().stream()
                .filter(l -> l.getName().contains(name) || name.contains(l.getShortName()))
                .findFirst();
        return league.map(League::getId).orElse(1L);
    }

    private Long getTeamIdByName(String name) {
        Optional<Team> team = teamRepository.findByStatusOrderByNameCn(1).stream()
                .filter(t -> t.getNameCn().contains(name) || name.contains(t.getNameCn()))
                .findFirst();
        return team.map(Team::getId).orElse(1L);
    }
}
