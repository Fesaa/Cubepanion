package art.ameliah.laby.addons.cubepanion.core.listener.games;

import net.labymod.api.event.Subscribe;
import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.config.imp.GameStatsTracker;
import art.ameliah.laby.addons.cubepanion.core.config.subconfig.StatsTrackerSubConfig;
import art.ameliah.laby.addons.cubepanion.core.events.GameEndEvent;
import art.ameliah.laby.addons.cubepanion.core.events.PlayerDeathEvent;
import art.ameliah.laby.addons.cubepanion.core.managers.CubepanionManager;

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
    GameStatsTracker tracker = config.getOrCreate(manager.getDivision());
    if (tracker == null) {
      return;
    }

    if (e.isClientPlayer()) {
      tracker.registerDeath(e.getKiller());
    } else {
      tracker.registerKill(e.getKilled());
    }
  }

  @Subscribe
  public void onGameWin(GameEndEvent e) {
    if (!config.isEnabled()) {
      return;
    }

    GameStatsTracker tracker = config.getOrCreate(e.getGame());
    if (tracker == null) {
      return;
    }

    if (e.hasWon()) {
      tracker.registerWin((int) e.getGameDuration());
    } else {
      tracker.registerLoss((int) e.getGameDuration());
    }
  }

}
