package org.cubepanion.core.listener.network;

import net.labymod.api.client.Minecraft;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.playerinfo.PlayerInfoRemoveEvent;
import net.labymod.api.event.client.network.playerinfo.PlayerInfoUpdateEvent;
import net.labymod.api.event.client.network.playerinfo.PlayerInfoUpdateEvent.UpdateType;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.config.subconfig.EndGameSubConfig;
import org.cubepanion.core.config.subconfig.EndGameSubConfig.GameEndMessage;
import org.cubepanion.core.managers.CubepanionManager;
import org.cubepanion.core.utils.CubeGame;
import java.util.UUID;

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
    if (this.manager.isPlaying(CubeGame.TEAM_EGGWARS)) {
      this.addon.rpcManager.registerDeath(e.playerInfo());
    }
  }

  @Subscribe
  public void onPlayerInfoUpdateEvent(PlayerInfoUpdateEvent e) {
    if (!this.addon.getManager().onCubeCraft()) {
      return;
    }
    if (e.type().equals(UpdateType.GAME_MODE)) {
      UUID uuid = e.playerInfo().profile().getUniqueId();
      switch (e.playerInfo().gameMode()) {
        case SURVIVAL -> {
          if (this.manager.isPlaying(CubeGame.TEAM_EGGWARS)) {
            this.manager.getSpawnProtectionManager().registerDeath(uuid);
          }
        }
        case SPECTATOR -> {
          if (!this.manager.getDivision().equals(CubeGame.TEAM_EGGWARS)) {
            this.addon.rpcManager.registerDeath(e.playerInfo());
            ClientPlayer player = addon.labyAPI().minecraft().getClientPlayer();
            Minecraft minecraft = addon.labyAPI().minecraft();
            if (player != null) {
              if (uuid.equals(player.getUniqueId())) {
                EndGameSubConfig config = addon.configuration().getAutomationConfig().getEndGameSubConfig();
                if (!config.getOnElimination().get()) {
                  return;
                }
                GameEndMessage gameEndMessage = config.getGameEndMessage().get();
                gameEndMessage.send(minecraft.chatExecutor(), config, manager.getPartyManager().isInParty());
                manager.setEliminated(true);
              }
            }
          }
        }
      }
    }
  }

}
