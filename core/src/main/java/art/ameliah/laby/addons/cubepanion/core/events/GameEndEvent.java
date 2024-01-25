package art.ameliah.laby.addons.cubepanion.core.events;

import net.labymod.api.event.Event;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;

/**
 * Fired when a CubeGame ends, if possible.
 * May be fired due to winning, leaving the game, or quiting the server.
 */
public class GameEndEvent implements Event {

  private final CubeGame game;

  private final long gameStartTime;
  private final boolean won;
  private final boolean switchedServer;
  private final long gameDuration;

  public GameEndEvent(CubeGame game, boolean won, boolean switchedServer, long gameStartTime) {
    this.game = game;
    this.gameStartTime = gameStartTime;
    this.won = won;
    this.switchedServer = switchedServer;
    this.gameDuration = System.currentTimeMillis() - this.gameStartTime;
  }

  /**
   * @return The game that has ended
   */
  public CubeGame getGame() {
    return game;
  }

  /**
   * @return The unix time when the game started
   */
  public long getGameStartTime() {
    return gameStartTime;
  }

  /**
   * @return If the player won
   */
  public boolean hasWon() {
    return won;
  }

  /**
   * Check this before you do anything that should happen on the server in which
   * the game was played. (Send chat message, ...)
   * @return Whether the player has already switched servers
   */
  public boolean hasSwitchedServer() {
    return switchedServer;
  }

  /**
   * @return Total play time
   */
  public long getGameDuration() {
    return gameDuration;
  }

}
