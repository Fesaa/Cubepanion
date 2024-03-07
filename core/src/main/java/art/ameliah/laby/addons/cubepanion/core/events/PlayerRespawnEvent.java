package art.ameliah.laby.addons.cubepanion.core.events;

import java.util.UUID;
import net.labymod.api.event.Event;

/**
 * Called when a player respawn (This is equivalent to a gamemode changes to survival) Or the
 * respawn message in case of the client player. Look Out! This means that the client player has two
 * RespawnEvents in EggWars per respawn.
 */
public class PlayerRespawnEvent implements Event {

  private final boolean isClientPlayer;
  private final UUID uuid;

  public PlayerRespawnEvent(boolean isClientPlayer, UUID uuid) {
    this.isClientPlayer = isClientPlayer;
    this.uuid = uuid;
  }

  /**
   * @return If the client player is respawning
   */
  public boolean isClientPlayer() {
    return this.isClientPlayer;
  }

  /**
   * @return The uuid of the respawning player
   */
  public UUID getUUID() {
    return this.uuid;
  }

}
