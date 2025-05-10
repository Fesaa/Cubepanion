package art.ameliah.laby.addons.cubepanion.core.listener.internal;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.events.GameEndEvent;
import art.ameliah.laby.addons.cubepanion.core.managers.CubepanionManager;
import net.labymod.api.Laby;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.ServerDisconnectEvent;
import net.labymod.api.event.client.network.server.ServerJoinEvent;

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
    if (!manager.isInPreLobby() && manager.hasLost()) {
      GameEndEvent event = new GameEndEvent(manager.getDivision(), false, true,
          manager.getGameStartTime());
      Laby.fireEvent(event);
    }
    this.manager.reset();
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
    if (address.contains("-dev-cc")) {
      this.manager.setDevServer(true);
      return true;
    }

    return false;
  }
}
