package org.cubepanion.core.listener.network;

import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.playerinfo.PlayerInfoRemoveEvent;
import net.labymod.api.event.client.network.playerinfo.PlayerInfoUpdateEvent;
import net.labymod.api.event.client.network.playerinfo.PlayerInfoUpdateEvent.UpdateType;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.managers.CubepanionManager;
import org.cubepanion.core.utils.CubeGame;

public class PlayerInfo {

  private final Cubepanion addon;
  private final CubepanionManager manager;

  public PlayerInfo(Cubepanion addon) {
    this.addon = addon;
    this.manager = addon.getManager();
  }

  @Subscribe
  public void onPlayerInfoRemoveEvent(PlayerInfoRemoveEvent e) {
    if (!this.addon.getManager().onCubeCraft()) {
      return;
    }
    if (!this.manager.isInPreLobby() && this.manager.getDivision().equals(CubeGame.TEAM_EGGWARS)) {
      this.addon.rpcManager.registerDeath(e.playerInfo());
    }
  }

  @Subscribe
  public void onPlayerInfoUpdateEvent(PlayerInfoUpdateEvent e) {
    if (!this.addon.getManager().onCubeCraft()) {
      return;
    }
    if (e.type().equals(UpdateType.GAME_MODE)) {
      switch (e.playerInfo().gameMode()) {
        case SURVIVAL -> {
          if (this.manager.getDivision().equals(CubeGame.TEAM_EGGWARS)
              && !this.manager.isInPreLobby()) {
            this.manager.getSpawnProtectionManager()
                .registerDeath(e.playerInfo().profile().getUniqueId());
          }
        }
        case SPECTATOR -> {
          if (!this.manager.getDivision().equals(CubeGame.TEAM_EGGWARS)) {
            this.addon.rpcManager.registerDeath(e.playerInfo());
          }
        }
      }
    }
  }

}
