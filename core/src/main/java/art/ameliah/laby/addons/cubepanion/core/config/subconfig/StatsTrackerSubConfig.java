package art.ameliah.laby.addons.cubepanion.core.config.subconfig;

import art.ameliah.laby.addons.cubepanion.core.config.imp.GameStatsTracker;
import art.ameliah.laby.addons.cubepanion.core.external.CubepanionAPI;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import java.util.Calendar;
import java.util.HashMap;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget.ButtonSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.ShowSettingInParent;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.util.MethodOrder;
import org.jetbrains.annotations.Nullable;

public class StatsTrackerSubConfig extends Config {

  @SwitchSetting
  @ShowSettingInParent
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(false);

  @SliderSetting(min = 0, max = 23)
  @SpriteSlot(x = 3, y = 1)
  private final ConfigProperty<Integer> resetTime = new ConfigProperty<>(5);

  @SwitchSetting
  @SpriteSlot(x = 5, y = 1)
  private final ConfigProperty<Boolean> keepPlayerStats = new ConfigProperty<>(true);

  @SwitchSetting
  @SpriteSlot(x = 5, y = 1)
  private final ConfigProperty<Boolean> keepSnapshots = new ConfigProperty<>(true);

  @SwitchSetting
  @SpriteSlot(x = 5, y = 1)
  private final ConfigProperty<Boolean> keepPerPlayerSnapshots = new ConfigProperty<>(false);

  @SliderSetting(min = 1, max = 20)
  @SpriteSlot(x = 7, y = 1)
  private final ConfigProperty<Integer> minEntry = new ConfigProperty<>(5);
  private final ConfigProperty<HashMap<CubeGame, GameStatsTracker>> gameStatsTrackers = new ConfigProperty<>(
      new HashMap<>());
  private final ConfigProperty<Long> lastReset = new ConfigProperty<>(
      Calendar.getInstance().getTime().getTime());

  @MethodOrder(after = "minEntry")
  @ButtonSetting
  @SpriteSlot(x = 6, y = 1)
  public void clearStatsTrackers() {
    for (GameStatsTracker gameStatsTracker : this.gameStatsTrackers.get().values()) {
      gameStatsTracker.cleanUp(this.minEntry.get());
    }
  }

  @MethodOrder(after = "clearStatsTrackers")
  @ButtonSetting
  @SpriteSlot(x = 6, y = 1)
  public void clearAllHistoricalPlayerData() {
    for (GameStatsTracker gameStatsTracker : this.gameStatsTrackers.get().values()) {
      gameStatsTracker.cleanUpPerPlayerHistorical();
    }
  }

  @MethodOrder(after = "clearAllHistoricalPlayerData")
  @ButtonSetting
  @SpriteSlot(x = 6, y = 1)
  public void clearAllPlayerData() {
    for (GameStatsTracker gameStatsTracker : this.gameStatsTrackers.get().values()) {
      gameStatsTracker.resetAllPlayerStats();
    }
  }

  public boolean isEnabled() {
    return enabled.get();
  }

  public HashMap<CubeGame, GameStatsTracker> getGameStatsTrackers() {
    return gameStatsTrackers.get();
  }

  public @Nullable GameStatsTracker getOrCreate(CubeGame cubeGame) {
    if (this.gameStatsTrackers.get().containsKey(cubeGame)) {
      return this.gameStatsTrackers.get().get(cubeGame);
    }
    var game = CubepanionAPI.I().tryGame(cubeGame.name());
    if (game != null && game.shouldTrack()) {
      GameStatsTracker gameStatsTracker = new GameStatsTracker(cubeGame);
      this.gameStatsTrackers.get().put(cubeGame, gameStatsTracker);
      return gameStatsTracker;
    }
    return null;
  }

  public ConfigProperty<Boolean> keepPlayerStats() {
    return this.keepPlayerStats;
  }

  public void checkForResets() {
    Calendar cal = Calendar.getInstance();
    int milliSecondsInASecond = 1000;
    int milliSecondsInAMinute = milliSecondsInASecond * 60;
    int milliSecondsInAHour = milliSecondsInAMinute * 60;

    int milliSecondsThisDay = cal.get(Calendar.HOUR_OF_DAY) * milliSecondsInAHour
        + cal.get(Calendar.MINUTE) * milliSecondsInAMinute
        + cal.get(Calendar.SECOND) * milliSecondsInASecond
        + cal.get(Calendar.MILLISECOND);

    long milliSecondsSinceLastReset = cal.getTime().getTime() - this.lastReset.get();
    boolean lastResetWasToday = milliSecondsSinceLastReset < milliSecondsThisDay;

    // Ensures milliSecondsSinceMidNightToLastReset is positive
    if (!lastResetWasToday && cal.get(Calendar.HOUR_OF_DAY) < this.resetTime.get()) {
      return;
    }

    long milliSecondsSinceMidNightToLastReset = milliSecondsThisDay - milliSecondsSinceLastReset;
    boolean lastResetWasBeforeResetTime =
        milliSecondsSinceMidNightToLastReset < this.resetTime.get() * milliSecondsInAHour;

    // Already reset today
    if (!lastResetWasBeforeResetTime) {
      return;
    }

    for (GameStatsTracker gameStatsTracker : this.gameStatsTrackers.get().values()) {
      gameStatsTracker.resetForDay(this.keepSnapshots.get(), this.keepPerPlayerSnapshots.get());
    }
    this.lastReset.set(cal.getTime().getTime());
  }


}
