package org.ccu.core.listener.network;

import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.playerinfo.PlayerInfoRemoveEvent;
import net.labymod.api.event.client.network.playerinfo.PlayerInfoUpdateEvent;
import net.labymod.api.event.client.network.playerinfo.PlayerInfoUpdateEvent.UpdateType;
import org.ccu.core.CCU;
import org.ccu.core.config.CCUManager;

public class PlayerInfo {

  private final CCU addon;
  private final CCUManager manager;

  public PlayerInfo(CCU addon) {
    this.addon = addon;
    this.manager = addon.getManager();
  }

  // Checks for players leaving the game. TODO: Why is this only checking in Team EggWars?
  @Subscribe
  public void onPlayerInfoRemoveEvent(PlayerInfoRemoveEvent e) {
    if (!this.manager.isInPreLobby() && this.manager.getDivisionName().equals("Team EggWars")) {
      this.addon.rpcManager.registerDeath(e.playerInfo());
    }
  }

  @Subscribe
  public void onPlayerInfoUpdateEvent(PlayerInfoUpdateEvent e) {
    if (e.type().equals(UpdateType.GAME_MODE)) {
      switch (e.playerInfo().gameMode()) {
        case SURVIVAL: {
          if (this.manager.getDivisionName().equals("Team EggWars")) {
            this.manager.getSpawnProtectionManager().registerDeath(e.playerInfo().profile().getUniqueId());
          }
          break;
        }
        case SPECTATOR: {
          if (!this.manager.getDivisionName().equals("Team EggWars")) {
            this.addon.rpcManager.registerDeath(e.playerInfo());
          }
          break;
        }
      }
    }
  }

}
