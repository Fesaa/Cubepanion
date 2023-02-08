package org.cubecraftutilities.core.config.imp;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import net.labymod.api.client.component.Component;
import org.cubecraftutilities.core.CCU;
import org.cubecraftutilities.core.Colours;
import org.jetbrains.annotations.NotNull;

public class GameStatsTracker {

  private final String game;
  private final StatsTracker winStreak;
  private final StatsTracker wins;
  private final StatsTracker played;
  private final StatsTracker kills;
  private final StatsTracker deaths;
  private HashMap<String, StatsTracker> perPlayerKills;

  private HashMap<String, StatsTracker> perPlayerDeaths;

  private final HashMap<String, GameStatsTracker> historicalData;

  public static boolean shouldMakeGameStatsTracker(String game) {
    return (game.equals("Solo SkyWars")
        || game.equals("Team EggWars")
        || game.equals("Lucky Islands")
        || game.equals("FFA"));
  }

  public GameStatsTracker(String game) {
    this.game = game;
    this.wins = new StatsTracker();
    this.played = new StatsTracker();
    this.winStreak = new StatsTracker();
    this.kills = new StatsTracker();
    this.deaths = new StatsTracker();
    this.perPlayerKills = new HashMap<>();
    this.perPlayerDeaths = new HashMap<>();
    this.historicalData = new HashMap<>();
  }

  private GameStatsTracker(String game, StatsTracker wins, StatsTracker played, StatsTracker winStreak, StatsTracker kills, StatsTracker deaths, HashMap<String, StatsTracker> perPlayerKills, HashMap<String, StatsTracker>perPlayerDeaths) {
    this.game = game;
    this.wins = wins;
    this.played = played;
    this.winStreak = winStreak;
    this.kills = kills;
    this.deaths = deaths;
    this.perPlayerKills = perPlayerKills;
    this.perPlayerDeaths = perPlayerDeaths;
    this.historicalData = new HashMap<>();
  }

  private GameStatsTracker Copy(boolean perPlayerSnapshots) {
    if (perPlayerSnapshots) {
      return new GameStatsTracker(this.game, this.wins, this.played, this.winStreak, this.kills, this.deaths, this.perPlayerKills, this.perPlayerDeaths);
    } else {
      return new GameStatsTracker(this.game, this.wins, this.played, this.winStreak, this.kills, this.deaths, new HashMap<>(), new HashMap<>());
    }

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

  // Historical Data Getters
  public HashMap<String, GameStatsTracker> getHistoricalData() {
    return this.historicalData;
  }

  public GameStatsTracker getHistoricalData(String date) {
    return this.historicalData.get(date);
  }

  // Registers
  public void registerWin() {
    this.wins.registerSuccess();
    this.played.registerSuccess();
    this.winStreak.registerSuccess();
  }

  public void registerLoss() {
    this.played.registerSuccess();
    this.winStreak.registerFail();
  }

  public void resetForDay(boolean snapshots, boolean perPlayerSnapshots) {
    if (snapshots) {
      this.historicalData.put(this.getDate(), this.Copy(perPlayerSnapshots));
    }
    this.wins.registerNewDay();
    this.played.registerNewDay();
    this.winStreak.registerNewDay();
    this.deaths.registerNewDay();
    this.kills.registerNewDay();
    for (StatsTracker perPlayerKills : this.perPlayerKills.values()) {
      perPlayerKills.registerNewDay();
    }
    for (StatsTracker perPlayerDeaths : this.perPlayerDeaths.values()) {
      perPlayerDeaths.registerNewDay();
    }
  }

  private String getDate() {
    Calendar cal = Calendar.getInstance();
    return cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH);
  }

  public void registerKill(String playerName) {
    this.kills.registerSuccess();

    if (!CCU.get().configuration().getStatsTrackerSubConfig().keepPlayerStats().get()) {
      return;
    }
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

    if (!CCU.get().configuration().getStatsTrackerSubConfig().keepPlayerStats().get()) {
      return;
    }
    StatsTracker playerDeathStatsTracker = this.perPlayerDeaths.get(playerName);
    if (playerDeathStatsTracker != null) {
      playerDeathStatsTracker.registerSuccess();
    } else {
      playerDeathStatsTracker = new StatsTracker();
      playerDeathStatsTracker.registerSuccess();
      this.perPlayerDeaths.put(playerName, playerDeathStatsTracker);
    }
  }

  // Cleanup functions
  public void cleanUp(int minEntry) {
    HashMap<String, StatsTracker> toRemove = new HashMap<>();
    for (Map.Entry<String, StatsTracker> set : this.perPlayerKills.entrySet()) {
      if (set.getValue().getAllTime() < minEntry) {
        toRemove.put(set.getKey(), set.getValue());
      }
    }

    for (Map.Entry<String, StatsTracker> set : toRemove.entrySet()) {
      this.perPlayerKills.remove(set.getKey(), set.getValue());
    }

    toRemove = new HashMap<>();
    for (Map.Entry<String, StatsTracker> set : this.perPlayerDeaths.entrySet()) {
      if (set.getValue().getAllTime() < minEntry) {
        toRemove.put(set.getKey(), set.getValue());
      }
    }

    for (Map.Entry<String, StatsTracker> set : toRemove.entrySet()) {
      this.perPlayerDeaths.remove(set.getKey(), set.getValue());
    }

    for (GameStatsTracker historicalGameStatsTracker : this.historicalData.values()) {
      historicalGameStatsTracker.cleanUp(minEntry);
    }

  }

  public void cleanUpPerPlayerHistorical() {
    for (String date : this.historicalData.keySet()) {
      GameStatsTracker gameStatsTracker = this.historicalData.get(date);
      gameStatsTracker.perPlayerKills = new HashMap<>();
      gameStatsTracker.perPlayerDeaths = new HashMap<>();
      this.historicalData.put(date, gameStatsTracker);
    }
  }

  public void resetAllPlayerStats() {
    this.perPlayerKills = new HashMap<>();
    this.perPlayerDeaths = new HashMap<>();
    this.cleanUpPerPlayerHistorical();
  }

  // Component generators
  public Component getUserStatsDisplayComponent(CCU addon, String name) {
    StatsTracker kills = this.perPlayerKills.get(name);
    StatsTracker deaths = this.perPlayerDeaths.get(name);

    Component userStatsDisplayComponent = addon.prefix()
        .append(Component.text("------- Interaction stats with " + name + " -------", Colours.Title));

    if (kills == null && deaths == null) {
      return addon.prefix()
          .append(Component.text("No interaction stats available in " + this.game + " with " + name, Colours.Error));
    }

    return userStatsDisplayComponent
        .append(getComponent(kills, "kills"))
        .append(getComponent(deaths, "deaths"));
  }

  @NotNull
  private Component getComponent(StatsTracker statsTracker, String type) {
    Component comp = Component.empty();
    if (statsTracker == null) {
      comp = comp.append(Component.text("\nNo " + type + " stats available.", Colours.Error));
    } else {
      comp = comp
          .append(Component.text("\nTotal " + type +": ", Colours.Primary))
          .append(Component.text(statsTracker.getAllTime(), Colours.Secondary))
          .append(Component.text("\nToday's " + type +": ", Colours.Primary))
          .append(Component.text(statsTracker.getDaily(), Colours.Secondary))
          .append(Component.text("\nTop Daily " + type +": ", Colours.Primary))
          .append(Component.text(statsTracker.getAllTimeDailyMax(), Colours.Secondary));
    }
    return comp;
  }

  public Component getDisplayComponent(CCU addon) {
    return addon.prefix()
        .append(Component.text("------ Game Stats For " + game + " ------", Colours.Title))
        .append(Component.text("\nTotal games played: ", Colours.Primary))
        .append(Component.text(this.getAllTimePlayed(), Colours.Secondary))
        .append(Component.text("\nToday's games played: ", Colours.Primary))
        .append(Component.text(this.getDailyPlayed(), Colours.Secondary))
        .append(Component.text("\nHighest all time win streak: ", Colours.Primary))
        .append(Component.text(this.getAllTimeHighestWinStreak(), Colours.Secondary))
        .append(Component.text("\nCurrent all time win streak: ", Colours.Primary))
        .append(Component.text(this.getWinStreak(), Colours.Secondary))
        .append(Component.text("\nHighest daily win streak: ", Colours.Primary))
        .append(Component.text(this.getDailyHighestWinStreak(), Colours.Secondary))
        .append(Component.text("\nToday's win streak: ", Colours.Primary))
        .append(Component.text(this.getDailyWinStreak(), Colours.Secondary))
        .append(Component.text("\nTotal wins: ", Colours.Primary))
        .append(Component.text(this.getAllTimeWins(), Colours.Secondary))
        .append(Component.text("\nToday's wins: ", Colours.Primary))
        .append(Component.text(this.getDailyWins(), Colours.Secondary))
        .append(Component.text("\nTotal loses: ", Colours.Primary))
        .append(Component.text(this.getAllTimePlayed() - this.getAllTimeWins(), Colours.Secondary))
        .append(Component.text("\nToday's loses: ", Colours.Primary))
        .append(Component.text(this.getDailyPlayed() - this.getDailyWins(), Colours.Secondary))
        .append(Component.text("\nTotal kills: ", Colours.Primary))
        .append(Component.text(this.getTotalKills(), Colours.Secondary))
        .append(Component.text("\nToday's kills: ", Colours.Primary))
        .append(Component.text(this.getDailyKills(), Colours.Secondary))
        .append(Component.text("\nTotal deaths: ", Colours.Primary))
        .append(Component.text(this.getTotalDeaths(), Colours.Secondary))
        .append(Component.text("\nToday's deaths: ", Colours.Primary))
        .append(Component.text(this.getDailyDeaths(), Colours.Secondary))
        ;
  }

}
