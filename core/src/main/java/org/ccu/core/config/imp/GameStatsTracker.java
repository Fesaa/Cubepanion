package org.ccu.core.config.imp;

public class GameStatsTracker {

  private int allTimeHighestWinStreak;
  private int dailyHighestWinStreak;
  private int currentWinStreak;
  private int dailyWinStreak;

  public GameStatsTracker() {
    this.currentWinStreak = 0;
    this.allTimeHighestWinStreak = 0;
    this.dailyHighestWinStreak = 0;
  }

  public int getCurrentWinStreak() {
    return currentWinStreak;
  }

  public int getDailyWinStreak() {
    return dailyWinStreak;
  }

  public int getAllTimeHighestWinStreak() {
    return allTimeHighestWinStreak;
  }

  public int getDailyHighestWinStreak() {
    return dailyHighestWinStreak;
  }

  public void registerWin() {
    this.currentWinStreak++;
    this.dailyWinStreak++;
    if (this.dailyWinStreak > this.dailyHighestWinStreak) {
      this.dailyHighestWinStreak = this.dailyWinStreak;
    }
    if (this.currentWinStreak > this.allTimeHighestWinStreak) {
      this.allTimeHighestWinStreak = this.currentWinStreak;
    }
  }

  public void lostWinStreak() {
    this.currentWinStreak = 0;
  }

  public void resetForDay() {
    if (this.currentWinStreak > this.allTimeHighestWinStreak) {
      this.allTimeHighestWinStreak = this.currentWinStreak;
    }

    this.dailyWinStreak = 0;
    this.dailyHighestWinStreak = 0;

  }
}
