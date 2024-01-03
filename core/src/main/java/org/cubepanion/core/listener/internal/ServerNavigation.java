package org.cubepanion.core.listener.internal;

import net.labymod.api.Laby;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.ServerDisconnectEvent;
import net.labymod.api.event.client.network.server.ServerJoinEvent;
import net.labymod.api.event.client.network.server.SubServerSwitchEvent;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.events.GameEndEvent;
import org.cubepanion.core.managers.CubepanionManager;

public class ServerNavigation {

  private final CubepanionManager manager;

  public ServerNavigation(Cubepanion addon) {
      this.manager = addon.getManager();
  }

  @Subscribe
  public void onServerJoinEvent(ServerJoinEvent e) {
    String serverAddress = e.serverData().address().toString();
    if (!isKubusMaken(serverAddress)) {
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
    GameEndEvent event = new GameEndEvent(manager.getDivision(), false, true,
        manager.getGameStartTime());
    Laby.fireEvent(event);
    this.manager.reset();
  }


  // This event is called when switching from server instance
  @Subscribe
  public void onSubServerSwitchEvent(SubServerSwitchEvent e) {
    if (!manager.onCubeCraft()) {
      return;
    }
    GameEndEvent event = new GameEndEvent(manager.getDivision(), false, true,
        manager.getGameStartTime());
    Laby.fireEvent(event);
    manager.onServerSwitch();
  }

  private boolean isKubusMaken(String address) {
    address = address.toLowerCase();
    if (address.endsWith("cubecraft.net")) {
      return true;
    }
    if (address.endsWith("cubecraftgames.net")) {
      return true;
    }
    if (address.endsWith("ccgn.co") && !address.contains("maps")) {
      return true;
    }
    return address.endsWith("-dev-cc");
  }
}
