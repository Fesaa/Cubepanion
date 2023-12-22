package org.cubepanion.core.listener.games;

import net.labymod.api.event.Subscribe;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.config.imp.GameStatsTracker;
import org.cubepanion.core.config.subconfig.StatsTrackerSubConfig;
import org.cubepanion.core.events.GameWinEvent;
import org.cubepanion.core.events.PlayerDeathEvent;
import org.cubepanion.core.managers.CubepanionManager;

public class Stats {

  private final Cubepanion addon;
  private final StatsTrackerSubConfig config;

  public Stats(Cubepanion addon) {
    this.addon = addon;
    this.config = addon.configuration().getStatsTrackerSubConfig();
  }

  @Subscribe
  public void onPlayerDeath(PlayerDeathEvent e) {
    if (!config.isEnabled()) {
      return;
    }

    CubepanionManager manager = this.addon.getManager();
    GameStatsTracker gameStatsTracker = config.getOrCreate(manager.getDivision());
    if (gameStatsTracker != null) {
      if (e.isClientPlayer()) {
        gameStatsTracker.registerDeath(e.getKiller());
      } else {
        gameStatsTracker.registerKill(e.getKilled());
      }
    }
  }

  @Subscribe
  public void onGameWin(GameWinEvent e) {
    if (!config.isEnabled()) {
      return;
    }

    GameStatsTracker tracker = config.getOrCreate(e.getGame());
    if (tracker == null) {
      return;
    }

    tracker.registerWin((int) e.getGameDuration());
  }

}
