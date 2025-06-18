package art.ameliah.laby.addons.cubepanion.core.listener;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.config.imp.GameStatsTracker;
import art.ameliah.laby.addons.cubepanion.core.config.subconfig.StatsTrackerSubConfig;
import art.ameliah.laby.addons.cubepanion.core.managers.CubepanionManager;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.lifecycle.GameShutdownEvent;

public class GameShutdownEventListener {

  private final Cubepanion addon;

  public GameShutdownEventListener(Cubepanion addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onGameShutdownEvent(GameShutdownEvent gameShutdownEvent) {
    if (this.addon.getManager().onCubeCraft()) {
      CubepanionManager manager = this.addon.getManager();
      StatsTrackerSubConfig statsTrackerSubConfig = this.addon.configuration()
          .getStatsTrackerSubConfig();
      if (statsTrackerSubConfig.isEnabled() && manager.hasLost() && !manager.isInPreLobby()) {
        GameStatsTracker gameStatsTracker = statsTrackerSubConfig.getGameStatsTrackers()
            .get(manager.getDivision());
        if (gameStatsTracker != null) {
          gameStatsTracker.registerLoss(
              (int) (System.currentTimeMillis() - manager.getGameStartTime()));
          gameStatsTracker.registerDeath("leave");
        }
      }
    }
    this.addon.saveConfiguration();
  }

}
