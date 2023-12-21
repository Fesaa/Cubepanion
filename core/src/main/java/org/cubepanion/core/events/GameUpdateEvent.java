package org.cubepanion.core.events;

import net.labymod.api.event.Event;
import org.cubepanion.core.utils.CubeGame;

public class GameUpdateEvent implements Event {

  private final CubeGame origin;

  private final CubeGame destination;

  private final boolean preLobby;

  public GameUpdateEvent(CubeGame origin, CubeGame destination, boolean preLobby) {
    this.origin = origin;
    this.destination = destination;
    this.preLobby = preLobby;
  }

  public CubeGame getOrigin() {
    return origin;
  }

  public CubeGame getDestination() {
    return destination;
  }

  public boolean isPreLobby() {
    return preLobby;
  }

}
