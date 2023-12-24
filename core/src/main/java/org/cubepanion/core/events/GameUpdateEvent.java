package org.cubepanion.core.events;

import net.labymod.api.event.Event;
import org.cubepanion.core.utils.CubeGame;

/**
 * Called when the division updates, or the game starts. GameEnds should be listened to with the
 * GameEndEvent
 */
public class GameUpdateEvent implements Event {

  private final CubeGame origin;

  private final CubeGame destination;

  private final boolean preLobby;
  private final boolean isSwitch;

  public GameUpdateEvent(CubeGame origin, CubeGame destination, boolean preLobby, boolean isSwitch) {
    this.origin = origin;
    this.destination = destination;
    this.preLobby = preLobby;
    this.isSwitch = isSwitch;
  }

  /**
   * @return Last division
   */
  public CubeGame getOrigin() {
    return origin;
  }

  /**
   * @return Current division
   */
  public CubeGame getDestination() {
    return destination;
  }

  /**
   * @return Game state
   */
  public boolean isPreLobby() {
    return preLobby;
  }

  /**
   * @return Whether this change is due to a server switch
   */
  public boolean isSwitch() {
    return isSwitch;
  }

}
