package org.cubepanion.core.listener.internal;

import java.util.UUID;
import net.labymod.api.Laby;
import net.labymod.api.client.entity.player.GameMode;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.playerinfo.PlayerInfoRemoveEvent;
import net.labymod.api.event.client.network.playerinfo.PlayerInfoUpdateEvent;
import net.labymod.api.event.client.network.playerinfo.PlayerInfoUpdateEvent.UpdateType;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.events.PlayerRespawnEvent;
import org.cubepanion.core.managers.CubepanionManager;
import org.cubepanion.core.utils.CubeGame;

public class PlayerInfo {

  private final Cubepanion addon;

  public PlayerInfo(Cubepanion addon) {
    this.addon = addon;
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
