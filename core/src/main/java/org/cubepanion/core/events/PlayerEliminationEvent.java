package org.cubepanion.core.events;

import net.labymod.api.event.Event;

public class PlayerEliminationEvent implements Event {

  private final boolean isClientPlayer;

  private final String name;

  public PlayerEliminationEvent(boolean isClientPlayer, String name) {
    this.isClientPlayer = isClientPlayer;
    this.name = name;
  }

  public boolean isClientPlayer() {
    return isClientPlayer;
  }

  public String getName() {
    return name;
  }

}
