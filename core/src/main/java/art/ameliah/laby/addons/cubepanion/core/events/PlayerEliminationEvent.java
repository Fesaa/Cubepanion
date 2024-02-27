package art.ameliah.laby.addons.cubepanion.core.events;

import net.labymod.api.event.Event;

/**
 * Called when a player is eliminated. This is only called in EggWars PlayerDeathEvent is still
 * called in EggWars (the event is stripped from chat)
 */
public class PlayerEliminationEvent implements Event {

  private final boolean isClientPlayer;

  private final String name;

  public PlayerEliminationEvent(boolean isClientPlayer, String name) {
    this.isClientPlayer = isClientPlayer;
    this.name = name;
  }

  /**
   * @return If the client player died
   */
  public boolean isClientPlayer() {
    return isClientPlayer;
  }

  /**
   * @return Name of the eliminated player
   */
  public String getName() {
    return name;
  }

}
