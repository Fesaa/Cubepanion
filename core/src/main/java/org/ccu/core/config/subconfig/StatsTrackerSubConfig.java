package org.ccu.core.config.subconfig;

import java.util.Calendar;
import java.util.HashMap;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.ParentSwitch;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import org.ccu.core.config.imp.GameStatsTracker;

@SuppressWarnings("FieldMayBeFinal")
public class StatsTrackerSubConfig extends Config {

  @ParentSwitch
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SliderSetting(min = 0, max = 23)
  private final ConfigProperty<Integer> resetTime = new ConfigProperty<>(5);

  private HashMap<String, GameStatsTracker> gameStatsTrackers = new HashMap<>();

  private final ConfigProperty<Long> lastReset = new ConfigProperty<>(Calendar.getInstance().getTime().getTime());

  public boolean isEnabled() {
    return enabled.get();
  }

  public HashMap<String, GameStatsTracker> getGameStatsTrackers() {
    return gameStatsTrackers;
  }

  public void checkForResets() {
    Calendar cal = Calendar.getInstance();
    int millisecondsInADay = 1000 * 60 * 60 * 24;

    if (cal.getTime().getTime() - this.lastReset.get() < millisecondsInADay) {
      return;
    }

    if (cal.get(Calendar.HOUR_OF_DAY) > this.resetTime.get()) {
      for (GameStatsTracker gameStatsTracker : this.gameStatsTrackers.values()) {
        gameStatsTracker.resetForDay();
      }
    }
    this.lastReset.set(cal.getTime().getTime());
  }


}
