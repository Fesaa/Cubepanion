package org.cubepanion.core.config.imp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import net.labymod.api.client.component.Component;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.gui.hud.widgets.GameTimerWidget.GameTimerConfig.layoutEnum;
import org.cubepanion.core.utils.Colours;
import org.cubepanion.core.utils.CubeGame;
import org.cubepanion.core.utils.I18nNamespaces;
import org.cubepanion.core.utils.Utils;
import org.jetbrains.annotations.NotNull;

public class GameStatsTracker {

  private final String mainKey = I18nNamespaces.globalNamespace + ".GameStatsTracker.";

  private final CubeGame game;
  private final StatsTracker winStreak;
  private final StatsTracker wins;
  private final StatsTracker played;
  private final StatsTracker kills;
  private final StatsTracker deaths;
  private final HashMap<String, StatsTracker> perPlayerKills;
  private final HashMap<String, StatsTracker> perPlayerDeaths;
  private final HashMap<String, GameStatsTracker> historicalData;
  private StatsTracker totalPlayTime;

  public GameStatsTracker(CubeGame game) {
    this.game = game;
    this.wins = new StatsTracker();
    this.played = new StatsTracker();
    this.winStreak = new StatsTracker();
    this.kills = new StatsTracker();
    this.deaths = new StatsTracker();
    this.perPlayerKills = new HashMap<>();
    this.perPlayerDeaths = new HashMap<>();
    this.historicalData = new HashMap<>();
    this.totalPlayTime = new StatsTracker();
  }

  private GameStatsTracker(CubeGame game, StatsTracker wins, StatsTracker played,
      StatsTracker winStreak, StatsTracker kills, StatsTracker deaths,
      HashMap<String, StatsTracker> perPlayerKills, HashMap<String, StatsTracker> perPlayerDeaths,
      StatsTracker totalPlayTime) {
    this.game = game;
    this.wins = wins;
    this.played = played;
    this.winStreak = winStreak;
    this.kills = kills;
    this.deaths = deaths;
    this.perPlayerKills = perPlayerKills;
    this.perPlayerDeaths = perPlayerDeaths;
    this.historicalData = new HashMap<>();
    this.totalPlayTime = Objects.requireNonNullElseGet(totalPlayTime, StatsTracker::new);
  }

  public static boolean shouldMakeGameStatsTracker(CubeGame game) {
    return (game.equals(CubeGame.SOLO_SKYWARS)
        || game.equals(CubeGame.TEAM_EGGWARS)
        || game.equals(CubeGame.SOLO_LUCKYISLANDS)
        || game.equals(CubeGame.FFA));
  }

  public CubeGame getGame() {
    return game;
  }

  private GameStatsTracker Copy(boolean perPlayerSnapshots) {
    if (perPlayerSnapshots) {
      return new GameStatsTracker(this.game, this.wins, this.played, this.winStreak, this.kills,
          this.deaths, this.perPlayerKills, this.perPlayerDeaths, this.totalPlayTime);
    } else {
      return new GameStatsTracker(this.game, this.wins, this.played, this.winStreak, this.kills,
          this.deaths, new HashMap<>(), new HashMap<>(), this.totalPlayTime);
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
  public void registerWin(int time) {
    this.wins.registerSuccess();
    this.played.registerSuccess();
    this.winStreak.registerSuccess();
    if (this.totalPlayTime == null) {
      this.totalPlayTime = new StatsTracker();
    }
    this.totalPlayTime.registerSuccess(time);
  }

  public void registerLoss(int time) {
    this.played.registerSuccess();
    this.winStreak.registerFail();
    if (this.totalPlayTime == null) {
      this.totalPlayTime = new StatsTracker();
    }
    this.totalPlayTime.registerSuccess(time);
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
    if (this.totalPlayTime != null) {
      this.totalPlayTime.registerNewDay();
    }
    for (StatsTracker perPlayerKills : this.perPlayerKills.values()) {
      perPlayerKills.registerNewDay();
    }
    for (StatsTracker perPlayerDeaths : this.perPlayerDeaths.values()) {
      perPlayerDeaths.registerNewDay();
    }
  }

  // Playtime
  public void registerPlayTime(int time) {
    if (this.totalPlayTime == null) {
      this.totalPlayTime = new StatsTracker();
    }
    this.totalPlayTime.registerSuccess(time);
  }

  public StatsTracker getTotalPlayTime() {
    return this.totalPlayTime;
  }

  private String getDate() {
    Calendar cal = Calendar.getInstance();
    return cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(
        Calendar.DAY_OF_MONTH);
  }

  public void registerKill(String playerName) {
    this.kills.registerSuccess();

    if (!Cubepanion.get().configuration().getStatsTrackerSubConfig().keepPlayerStats().get()) {
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

    if (!Cubepanion.get().configuration().getStatsTrackerSubConfig().keepPlayerStats().get()) {
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
    ArrayList<String> toRemove = new ArrayList<>();
    for (Map.Entry<String, StatsTracker> set : this.perPlayerKills.entrySet()) {
      if (set.getValue().getAllTime() < minEntry) {
        toRemove.add(set.getKey());
      }
    }

    for (String key : toRemove) {
      this.perPlayerKills.remove(key);
    }

    toRemove.clear();
    for (Map.Entry<String, StatsTracker> set : this.perPlayerDeaths.entrySet()) {
      if (set.getValue().getAllTime() < minEntry) {
        toRemove.add(set.getKey());
      }
    }

    for (String key : toRemove) {
      this.perPlayerDeaths.remove(key);
    }

    for (GameStatsTracker historicalGameStatsTracker : this.historicalData.values()) {
      historicalGameStatsTracker.cleanUp(minEntry);
    }

  }

  public void cleanUpPerPlayerHistorical() {
    for (String date : this.historicalData.keySet()) {
      GameStatsTracker gameStatsTracker = this.historicalData.get(date);
      gameStatsTracker.perPlayerKills.clear();
      gameStatsTracker.perPlayerDeaths.clear();
      this.historicalData.put(date, gameStatsTracker);
    }
  }

  public void resetAllPlayerStats() {
    this.perPlayerKills.clear();
    this.perPlayerDeaths.clear();
    this.cleanUpPerPlayerHistorical();
  }

  // Component generators
  public Component getUserStatsDisplayComponent(String name) {
    StatsTracker kills = this.perPlayerKills.get(name);
    StatsTracker deaths = this.perPlayerDeaths.get(name);

    Component userStatsDisplayComponent = Component.translatable(
            this.mainKey + "interactionStats.title", Component.text(name, Colours.Primary))
        .color(Colours.Title);

    if (kills == null && deaths == null) {
      return Component.translatable(this.mainKey + "interactionStats.notFound",
          Component.text(this.game.getString()), Component.text(name)).color(Colours.Error);
    }

    return userStatsDisplayComponent
        .append(getComponent(kills, "kills"))
        .append(getComponent(deaths, "deaths"));
  }

  @NotNull
  private Component getComponent(StatsTracker statsTracker, String type) {
    Component comp = Component.empty();
    if (statsTracker == null) {
      comp = comp.append(Component.translatable(this.mainKey + "interactionStats.notAvailable",
          Component.text(type)).color(Colours.Error));
    } else {
      comp = comp
          .append(
              Component.translatable(this.mainKey + "interactionStats.stats",
                      Component.text(type),
                      Component.text(statsTracker.getAllTime(), Colours.Secondary),
                      Component.text(type), Component.text(statsTracker.getDaily(), Colours.Secondary),
                      Component.text(type),
                      Component.text(statsTracker.getAllTimeDailyMax(), Colours.Secondary))
                  .color(Colours.Primary));
    }
    return comp;
  }

  public Component getDisplayComponent() {
    if (this.totalPlayTime == null) {
      this.totalPlayTime = new StatsTracker();
    }
    return
        Component.translatable(this.mainKey + "gameStats.titleToday",
                Component.text(game.getString(), Colours.Primary))
            .color(Colours.Title)
            .append(Component.translatable(this.mainKey + "gameStats.statsToday",
                Component.text(this.getDailyPlayed(), Colours.Secondary),
                Component.text(
                    Utils.getFormattedString(this.totalPlayTime.getDaily(), layoutEnum.WORDS),
                    Colours.Secondary),
                Component.text(this.getWinStreak(), Colours.Secondary),
                Component.text(this.getDailyWinStreak(), Colours.Secondary),
                Component.text(this.getDailyWins(), Colours.Secondary),
                Component.text(this.getDailyPlayed() - this.getDailyWins(), Colours.Secondary),
                Component.text(this.getDailyKills(), Colours.Secondary),
                Component.text(this.getDailyDeaths(), Colours.Secondary)
            ).color(Colours.Primary))
            .append(Component.translatable(this.mainKey + "gameStats.titleAllTime",
                    Component.text(game.getString(), Colours.Primary))
                .color(Colours.Title))
            .append(Component.translatable(this.mainKey + "gameStats.statsAllTime",
                Component.text(this.getAllTimePlayed(), Colours.Secondary),
                Component.text(
                    Utils.getFormattedString(this.totalPlayTime.getAllTime(), layoutEnum.WORDS),
                    Colours.Secondary),
                Component.text(this.getAllTimeHighestWinStreak(), Colours.Secondary),
                Component.text(this.getDailyHighestWinStreak(), Colours.Secondary),
                Component.text(this.getAllTimeWins(), Colours.Secondary),
                Component.text(this.getAllTimePlayed() - this.getAllTimeWins(), Colours.Secondary),
                Component.text(this.getTotalKills(), Colours.Secondary),
                Component.text(this.getTotalDeaths(), Colours.Secondary)
            ).color(Colours.Primary));
  }
}
