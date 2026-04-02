package com.lottery.service.datasource;

import com.lottery.entity.MatchInfo;
import com.lottery.entity.Team;
import java.util.List;

public interface SportsDataClient {
    String getSourceKey();

    List<Team> fetchTeams() throws Exception;

    List<MatchInfo> fetchMatches(String date) throws Exception;

    boolean isEnabled();
}
