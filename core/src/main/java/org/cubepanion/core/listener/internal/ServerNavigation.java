package org.cubepanion.core.listener.internal;

import net.labymod.api.Laby;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.ServerDisconnectEvent;
import net.labymod.api.event.client.network.server.ServerJoinEvent;
import net.labymod.api.event.client.network.server.SubServerSwitchEvent;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.config.imp.GameStatsTracker;
import org.cubepanion.core.config.subconfig.StatsTrackerSubConfig;
import org.cubepanion.core.events.GameEndEvent;
import org.cubepanion.core.managers.CubepanionManager;

public class ServerNavigation {

  private final Cubepanion addon;
  private final CubepanionManager manager;

  public ServerNavigation(Cubepanion addon) {
    this.addon = addon;
    this.manager = addon.getManager();
  }

  @Subscribe
  public void onServerJoinEvent(ServerJoinEvent e) {
    String serverAddress = e.serverData().address().toString().toLowerCase();
    if (!(serverAddress.contains("cubecraft") || (serverAddress.contains("ccgn.co")
        && !serverAddress.contains("maps")) || serverAddress.contains("dev-cc"))) {
      this.manager.reset();
      return;
    }
    this.manager.onCubeJoin();
  }

  @Subscribe
  public void onServerDisconnectEvent(ServerDisconnectEvent e) {
    if (!manager.onCubeCraft()) {
      return;
    }
    GameEndEvent event = new GameEndEvent(manager.getDivision(), false, true, manager.getGameStartTime());
    Laby.fireEvent(event);
    this.manager.reset();
  }


  // This event is called when switching from server instance
  @Subscribe
  public void onSubServerSwitchEvent(SubServerSwitchEvent e) {
    if (!manager.onCubeCraft()) {
      return;
    }
    GameEndEvent event = new GameEndEvent(manager.getDivision(), false, true, manager.getGameStartTime());
    Laby.fireEvent(event);
    manager.onServerSwitch();
  }
}
