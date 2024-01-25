package art.ameliah.laby.addons.cubepanion.core.listener.internal;

import java.util.UUID;
import net.labymod.api.Laby;
import net.labymod.api.client.entity.player.GameMode;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.playerinfo.PlayerInfoUpdateEvent;
import net.labymod.api.event.client.network.playerinfo.PlayerInfoUpdateEvent.UpdateType;
import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.events.PlayerRespawnEvent;

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
