package art.ameliah.laby.addons.cubepanion.core.events;

import art.ameliah.laby.addons.cubepanion.core.external.Game;
import net.labymod.api.event.Event;

/**
 * Called when the division updatesGameEnds should be listened to with the
 * GameEndEvent
 */
public class GameJoinEvent implements Event {

  private final Game origin;

  private final Game destination;

  private final boolean preLobby;

  public GameJoinEvent(Game origin, Game destination, boolean preLobby) {
    this.origin = origin;
    this.destination = destination;
    this.preLobby = preLobby;
  }

  /**
   * @return Last division
   */
  public Game getOrigin() {
    return origin;
  }

  /**
   * @return Current division
   */
  public Game getDestination() {
    return destination;
  }

  /**
   * @return Game state
   */
  public boolean isPreLobby() {
    return preLobby;
  }

}
