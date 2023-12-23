package org.cubepanion.core.listener.internal;

import java.util.Objects;
import java.util.UUID;
import net.labymod.api.Laby;
import net.labymod.api.client.Minecraft;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.entity.player.GameMode;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.playerinfo.PlayerInfoRemoveEvent;
import net.labymod.api.event.client.network.playerinfo.PlayerInfoUpdateEvent;
import net.labymod.api.event.client.network.playerinfo.PlayerInfoUpdateEvent.UpdateType;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.config.subconfig.EndGameSubConfig;
import org.cubepanion.core.config.subconfig.EndGameSubConfig.GameEndMessage;
import org.cubepanion.core.events.PlayerRespawnEvent;
import org.cubepanion.core.managers.CubepanionManager;
import org.cubepanion.core.managers.DiscordAPI;
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
    if (this.manager.isPlaying(CubeGame.TEAM_EGGWARS)) {
      DiscordAPI.getInstance().registerDeath(e.playerInfo());
    }
  }

  @Subscribe
  public void onPlayerInfoUpdateEvent(PlayerInfoUpdateEvent e) {
    if (!this.addon.getManager().onCubeCraft()) {
      return;
    }
    UUID uuid = e.playerInfo().profile().getUniqueId();
    boolean isClientPlayer = uuid.equals(SessionTracker.get().uuid());

    if (e.type().equals(UpdateType.GAME_MODE)) {
        if (e.playerInfo().gameMode() == GameMode.SURVIVAL) {
            Laby.fireEvent(new PlayerRespawnEvent(isClientPlayer, uuid));
        }
    }
  }

}
