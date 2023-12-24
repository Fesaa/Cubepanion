package org.cubepanion.core.events;

import java.util.UUID;
import net.labymod.api.event.Event;

public class PlayerRespawnEvent implements Event {

  private final boolean isClientPlayer;
  private final UUID uuid;

  public PlayerRespawnEvent(boolean isClientPlayer, UUID uuid) {
    this.isClientPlayer = isClientPlayer;
    this.uuid = uuid;
  }

  public boolean isClientPlayer() {
    return this.isClientPlayer;
  }

  public UUID getUUID() {
    return this.uuid;
  }

}
