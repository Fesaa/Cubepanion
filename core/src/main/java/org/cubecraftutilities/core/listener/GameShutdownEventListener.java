package org.cubecraftutilities.core.listener;

import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.lifecycle.GameShutdownEvent;
import org.cubecraftutilities.core.CCU;
import org.cubecraftutilities.core.config.imp.GameStatsTracker;
import org.cubecraftutilities.core.config.subconfig.StatsTrackerSubConfig;
import org.cubecraftutilities.core.managers.CCUManager;

public class GameShutdownEventListener {

  private final CCU addon;

  public GameShutdownEventListener(CCU addon) {this.addon = addon;}

  @Subscribe
  public void onGameShutdownEvent(GameShutdownEvent gameShutdownEvent) {
    CCUManager manager = this.addon.getManager();
    StatsTrackerSubConfig statsTrackerSubConfig = this.addon.configuration().getStatsTrackerSubConfig();
    if (statsTrackerSubConfig.isEnabled() && !manager.isWon() && !manager.isInPreLobby()) {
      GameStatsTracker gameStatsTracker = statsTrackerSubConfig.getGameStatsTrackers().get(manager.getDivisionName());
      if (gameStatsTracker != null) {
        gameStatsTracker.registerLoss();
        gameStatsTracker.registerDeath("leave");
      }
    }
    this.addon.saveConfiguration();
  }

}
