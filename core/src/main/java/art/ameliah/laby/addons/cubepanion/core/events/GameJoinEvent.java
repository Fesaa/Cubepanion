package art.ameliah.laby.addons.cubepanion.core.events;

import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import net.labymod.api.event.Event;

/**
 * Called when the division updatesGameEnds should be listened to with the
 * GameEndEvent
 */
public class GameJoinEvent implements Event {

  private final CubeGame origin;

  private final CubeGame destination;

  private final boolean preLobby;

  public GameJoinEvent(CubeGame origin, CubeGame destination, boolean preLobby) {
    this.origin = origin;
    this.destination = destination;
    this.preLobby = preLobby;
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

}
