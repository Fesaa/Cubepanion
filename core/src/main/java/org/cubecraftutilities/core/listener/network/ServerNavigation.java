package org.cubecraftutilities.core.listener.network;

import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.ServerDisconnectEvent;
import net.labymod.api.event.client.network.server.ServerJoinEvent;
import net.labymod.api.event.client.network.server.SubServerSwitchEvent;
import org.cubecraftutilities.core.CCU;
import org.cubecraftutilities.core.config.imp.GameStatsTracker;
import org.cubecraftutilities.core.config.subconfig.StatsTrackerSubConfig;
import org.cubecraftutilities.core.managers.CCUManager;

public class ServerNavigation {

  private final CCU addon;
  private final CCUManager manager;

  public ServerNavigation(CCU addon) {
    this.addon = addon;
    this.manager = addon.getManager();
  }

  @Subscribe
  public void onServerJoinEvent(ServerJoinEvent e) {
    String serverAddress = e.serverData().address().toString();
    if (!(serverAddress.contains("cubecraft") || serverAddress.contains("ccgn.co"))) {
      this.manager.reset();
      return;
    }
    this.manager.onCubeJoin();
    this.executeWhereAmI();
  }

  @Subscribe
  public void onServerDisconnectEvent(ServerDisconnectEvent e) {
    if (this.manager.onCubeCraft()) {
      register_game_leave();
      this.manager.reset();
      this.addon.rpcManager.removeCustomRPC();
    }
  }

  private void register_game_leave() {
    StatsTrackerSubConfig statsTrackerSubConfig = this.addon.configuration().getStatsTrackerSubConfig();
    if (statsTrackerSubConfig.isEnabled() && this.manager.hasLost() && !this.manager.isInPreLobby()) {
      GameStatsTracker gameStatsTracker = statsTrackerSubConfig.getGameStatsTrackers().get(this.manager.getDivision());
      if (gameStatsTracker != null) {
        gameStatsTracker.registerLoss();
        gameStatsTracker.registerDeath("leave");
      }
    }
  }

  // This event is called when switching from server instance
  @Subscribe
  public void onSubServerSwitchEvent(SubServerSwitchEvent e) {
    if (!this.manager.onCubeCraft()) {
      return;
    }
    this.manager.setHasUpdatedAfterServerSwitch(false);
    register_game_leave();
    this.executeWhereAmI();
    this.manager.onServerSwitch();
  }

  private void executeWhereAmI() {
    if (this.addon.configuration().getAutomationConfig().displayWhereAmI().get()) {
      this.addon.labyAPI().minecraft().chatExecutor().chat("/whereami", false);
    }
  }
}
