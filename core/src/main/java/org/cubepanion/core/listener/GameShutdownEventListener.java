package org.cubepanion.core.listener;

import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.lifecycle.GameShutdownEvent;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.config.imp.GameStatsTracker;
import org.cubepanion.core.config.subconfig.StatsTrackerSubConfig;
import org.cubepanion.core.managers.CubepanionManager;

public class GameShutdownEventListener {

  private final Cubepanion addon;

  public GameShutdownEventListener(Cubepanion addon) {this.addon = addon;}

  @Subscribe
  public void onGameShutdownEvent(GameShutdownEvent gameShutdownEvent) {
    CubepanionManager manager = this.addon.getManager();
    StatsTrackerSubConfig statsTrackerSubConfig = this.addon.configuration().getStatsTrackerSubConfig();
    if (statsTrackerSubConfig.isEnabled() && manager.hasLost() && !manager.isInPreLobby()) {
      GameStatsTracker gameStatsTracker = statsTrackerSubConfig.getGameStatsTrackers().get(manager.getDivision());
      if (gameStatsTracker != null) {
        gameStatsTracker.registerLoss();
        gameStatsTracker.registerDeath("leave");
      }
    }
    this.addon.saveConfiguration();
  }

}
