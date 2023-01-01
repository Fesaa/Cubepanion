package org.ccu.core.config.imp;

import java.util.HashMap;

public class GameStatsTracker {

  private final StatsTracker winStreak;
  private final StatsTracker wins;
  private final StatsTracker played;
  private final StatsTracker kills;
  private final StatsTracker deaths;
  private final HashMap<String, StatsTracker> perPlayerKills;

  private final HashMap<String, StatsTracker> perPlayerDeaths;

  public GameStatsTracker() {
    this.wins = new StatsTracker();
    this.played = new StatsTracker();
    this.winStreak = new StatsTracker();
    this.kills = new StatsTracker();
    this.deaths = new StatsTracker();
    this.perPlayerKills = new HashMap<>();
    this.perPlayerDeaths = new HashMap<>();
  }

  // Games Played Getters
  public int getDailyPlayed() {
    return this.played.getDaily();
  }

  public int getMaxDailyPlayed() {
    return this.played.getAllTimeDailyMax();
  }

  public int getAllTimePlayed() {
    return this.played.getAllTime();
  }

  // Win Getters
  public int getDailyWins() {
    return this.wins.getDaily();
  }

  public int getMaxDailyWins() {
    return this.wins.getAllTimeDailyMax();
  }

  public int getAllTimeWins() {
    return this.wins.getAllTime();
  }


  // Win Streak Getters
  public int getWinStreak() {
    return this.winStreak.getAllTime();
  }

  public int getDailyWinStreak() {
    return this.winStreak.getDaily();
  }

  public int getAllTimeHighestWinStreak() {
    return this.winStreak.getAllTimeMax();
  }

  public int getDailyHighestWinStreak() {
    return this.winStreak.getAllTimeDailyMax();
  }

  // Kill getters
  public int getTotalKills() {
    return this.kills.getAllTime();
  }

  public int getDailyKills() {
    return this.kills.getDaily();
  }

  public int getPlayerKills(String playerName) {
    StatsTracker playerStatsTracker = this.perPlayerKills.get(playerName);
    if (playerStatsTracker == null) {
      return 0;
    }
    return playerStatsTracker.getAllTime();
  }

  public int getDailyPlayerKills(String playerName) {
    StatsTracker playerStatsTracker = this.perPlayerKills.get(playerName);
    if (playerStatsTracker == null) {
      return 0;
    }
    return playerStatsTracker.getDaily();
  }

  // Death getters
  public int getTotalDeaths() {
    return this.deaths.getAllTime();
  }

  public int getDailyDeaths() {
    return this.deaths.getDaily();
  }

  public int getPlayerDeaths(String playerName) {
    StatsTracker playerStatsTracker = this.perPlayerDeaths.get(playerName);
    if (playerStatsTracker == null) {
      return 0;
    }
    return playerStatsTracker.getAllTime();
  }

  public int getDailyPlayerDeaths(String playerName) {
    StatsTracker playerStatsTracker = this.perPlayerDeaths.get(playerName);
    if (playerStatsTracker == null) {
      return 0;
    }
    return playerStatsTracker.getDaily();
  }

  public void registerWin() {
    this.wins.registerSuccess();
    this.played.registerSuccess();
    this.winStreak.registerSuccess();
  }

  public void registerLoss() {
    this.played.registerSuccess();
    this.winStreak.registerFail();
  }

  public void resetForDay() {
    this.wins.registerNewDay();
    this.played.registerNewDay();
    this.winStreak.registerNewDay();
  }

  public void registerKill(String playerName) {
    this.kills.registerSuccess();
    StatsTracker playerKillStatsTracker = this.perPlayerKills.get(playerName);
    if (playerKillStatsTracker != null) {
      playerKillStatsTracker.registerSuccess();
    } else {
      playerKillStatsTracker = new StatsTracker();
      playerKillStatsTracker.registerSuccess();
      this.perPlayerKills.put(playerName, playerKillStatsTracker);
    }
  }

  public void registerDeath(String playerName) {
    this.deaths.registerSuccess();
    StatsTracker playerKillStatsTracker = this.perPlayerDeaths.get(playerName);
    if (playerKillStatsTracker != null) {
      playerKillStatsTracker.registerSuccess();
    } else {
      playerKillStatsTracker = new StatsTracker();
      playerKillStatsTracker.registerSuccess();
      this.perPlayerDeaths.put(playerName, playerKillStatsTracker);
    }
  }
}
