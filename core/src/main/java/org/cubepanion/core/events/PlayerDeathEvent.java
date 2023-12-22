package org.cubepanion.core.events;

import net.labymod.api.event.Event;

public class PlayerDeathEvent implements Event {

  private final boolean isClientPlayer;

  private final String killer;

  private final String killed;

  public PlayerDeathEvent(boolean isClientPlayer, String killer, String killed) {
    this.isClientPlayer = isClientPlayer;
    this.killer = killer;
    this.killed = killed;
  }

  public boolean isClientPlayer() {
    return isClientPlayer;
  }

  public String getKiller() {
    return killer;
  }

  public String getKilled() {
    return killed;
  }

}
