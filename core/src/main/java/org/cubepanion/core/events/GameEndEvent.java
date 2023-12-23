package org.cubepanion.core.events;

import net.labymod.api.event.Event;
import org.cubepanion.core.utils.CubeGame;

public class GameEndEvent implements Event {

  private final CubeGame game;

  private final long gameStartTime;
  private final boolean won;
  private final boolean switchedServer;

  public GameEndEvent(CubeGame game, boolean won, boolean switchedServer, long gameStartTime) {
    this.game = game;
    this.gameStartTime = gameStartTime;
    this.won = won;
    this.switchedServer = switchedServer;
  }

  public CubeGame getGame() {
    return game;
  }

  public long getGameStartTime() {
    return gameStartTime;
  }

  public boolean hasWon() {
    return won;
  }

  public boolean hasSwitchedServer() {
    return switchedServer;
  }

  public long getGameDuration() {
    return System.currentTimeMillis() - gameStartTime;
  }

}
