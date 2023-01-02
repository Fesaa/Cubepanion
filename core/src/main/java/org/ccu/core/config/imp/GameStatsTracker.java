package org.ccu.core.config.imp;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.ccu.core.CCU;
import org.jetbrains.annotations.NotNull;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class GameStatsTracker {

  private final String game;
  private final StatsTracker winStreak;
  private final StatsTracker wins;
  private final StatsTracker played;
  private final StatsTracker kills;
  private final StatsTracker deaths;
  private final HashMap<String, StatsTracker> perPlayerKills;

  private final HashMap<String, StatsTracker> perPlayerDeaths;

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
    for (Map.Entry<String, StatsTracker> set : this.perPlayerKills.entrySet()) {
      if (set.getValue().getAllTime() < minEntry) {
        this.perPlayerKills.remove(set.getKey(), set.getValue());
      }
    }
    for (Map.Entry<String, StatsTracker> set : this.perPlayerDeaths.entrySet()) {
      if (set.getValue().getAllTime() < minEntry) {
        this.perPlayerDeaths.remove(set.getKey(), set.getValue());
      }
    }

    for (GameStatsTracker historicalGameStatsTracker : this.historicalData.values()) {
      historicalGameStatsTracker.cleanUp(minEntry);
    }

  }

  // Component generators
  public Component getUserStatsDisplayComponent(CCU addon, String name) {
    StatsTracker kills = this.perPlayerKills.get(name);
    StatsTracker deaths = this.perPlayerDeaths.get(name);

    Component userStatsDisplayComponent = Component.empty();

    if (kills == null && deaths == null) {
      return userStatsDisplayComponent.append(addon.prefix())
                                      .append(Component.text("No interaction stats available in " + this.game + " with " + name, NamedTextColor.RED));
    }

    userStatsDisplayComponent = userStatsDisplayComponent.append(addon.prefix())
        .append(Component.text("Interaction stats in " + this.game + " with " + name, NamedTextColor.GREEN));

    userStatsDisplayComponent = userStatsDisplayComponent.append(Component.text("\n    •Kills", NamedTextColor.AQUA));
    userStatsDisplayComponent = getComponent(kills, userStatsDisplayComponent, "kills");

    userStatsDisplayComponent = userStatsDisplayComponent.append(Component.text("\n    •Deaths", NamedTextColor.AQUA));
    userStatsDisplayComponent = getComponent(deaths, userStatsDisplayComponent, "deaths");

    return userStatsDisplayComponent;
  }

  @NotNull
  private Component getComponent(StatsTracker statsTracker, Component userStatsDisplayComponent, String type) {
    if (statsTracker == null) {
      userStatsDisplayComponent = userStatsDisplayComponent.append(Component.text("\n        N/A", NamedTextColor.GRAY));
    } else {
      userStatsDisplayComponent = userStatsDisplayComponent
          .append(Component.text("\n        -Total " + type + ": " + statsTracker.getAllTime(), NamedTextColor.GRAY))
          .append(Component.text("\n        -Today's " + type + ": " + statsTracker.getDaily(), NamedTextColor.GRAY))
          .append(Component.text("\n        -Most " + type + " in a day: " + statsTracker.getAllTimeDailyMax(), NamedTextColor.GRAY));
    }
    return userStatsDisplayComponent;
  }

  public Component getDisplayComponent(CCU addon) {
    return Component.empty()
        .append(addon.prefix())
        .append(Component.text("Recorded stats for " + this.game + "\n", NamedTextColor.DARK_AQUA)
            .hoverEvent(HoverEvent.showText(Component.text("You can use /stats <YYYY-MM-DD> for a snapshot of your stats on that day", NamedTextColor.GREEN))))

        .append(Component.text("\n    •Games Played ", NamedTextColor.AQUA))
        .append(Component.text("\n        -Total games played: " + this.getAllTimePlayed(), NamedTextColor.GRAY))
        .append(Component.text("\n        -Today's games played: " + this.getDailyPlayed(), NamedTextColor.GRAY))

        .append(Component.text("\n    •Win Streak ", NamedTextColor.AQUA))
        .append(Component.text("\n        -Highest all time win streak: " + this.getAllTimeHighestWinStreak(), NamedTextColor.GRAY))
        .append(Component.text("\n        -Current all time win streak: " + this.getWinStreak(), NamedTextColor.GRAY))
        .append(Component.text("\n        -Highest daily win streak: " + this.getDailyHighestWinStreak(), NamedTextColor.GRAY))
        .append(Component.text("\n        -Today's win streak: " + this.getDailyWinStreak(), NamedTextColor.GRAY))

        .append(Component.text("\n    •Wins ", NamedTextColor.AQUA))
        .append(Component.text("\n        -Total wins: " + this.getAllTimeWins(), NamedTextColor.GRAY))
        .append(Component.text("\n        -Today's wins: " + this.getDailyWins(), NamedTextColor.GRAY))

        .append(Component.text("\n    •Loses ", NamedTextColor.AQUA))
        .append(Component.text("\n        -Total loses: " + (this.getAllTimePlayed() - this.getAllTimeWins()), NamedTextColor.GRAY))
        .append(Component.text("\n        -Today's loses: " + (this.getDailyPlayed() - this.getDailyWins()), NamedTextColor.GRAY))

        .append(Component.text("\n    •Kills ", NamedTextColor.AQUA)
            .hoverEvent(HoverEvent.showText(Component.text("You can use /stats <user name> for your kills on and death to that player.", NamedTextColor.GREEN))))
        .append(Component.text("\n        -Total kills: " + this.getTotalKills(), NamedTextColor.GRAY))
        .append(Component.text("\n        -Today's kills: " + this.getDailyKills(), NamedTextColor.GRAY))

        .append(Component.text("\n    •Deaths ", NamedTextColor.AQUA)
            .hoverEvent(HoverEvent.showText(Component.text("You can use /stats <user name> for your kills on and death to that player.", NamedTextColor.GREEN))))
        .append(Component.text("\n        -Total deaths: " + this.getTotalDeaths(), NamedTextColor.GRAY))
        .append(Component.text("\n        -Today's deaths: " + this.getDailyDeaths(), NamedTextColor.GRAY));
  }



}
