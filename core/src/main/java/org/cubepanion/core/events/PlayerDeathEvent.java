package org.cubepanion.core.events;

import net.labymod.api.event.Event;

/**
 * Called when the player dies, or kills someone (the event is stripped from chat)
 */
public class PlayerDeathEvent implements Event {

  private final boolean isClientPlayer;

  private final String killer;

  private final String killed;

  public PlayerDeathEvent(boolean isClientPlayer, String killer, String killed) {
    this.isClientPlayer = isClientPlayer;
    this.killer = killer;
    this.killed = killed;
  }

  /**
   * @return If the client player died
   */
  public boolean isClientPlayer() {
    return isClientPlayer;
  }

  /**
   * This may be a non player, if isClientPlayer is True
   * Such as; `void`, `leave`, `tnt`, etc.
   * @return Who made the kill
   */
  public String getKiller() {
    return killer;
  }

  /**
   * @return Person who died
   */
  public String getKilled() {
    return killed;
  }

}
