package org.ccu.core.config.imp;

public class GameStatsTracker {

  private final StatsTracker winStreak;
  private final StatsTracker wins;
  private final StatsTracker played;

  public GameStatsTracker() {
    this.wins = new StatsTracker();
    this.played = new StatsTracker();
    this.winStreak = new StatsTracker();
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
}
