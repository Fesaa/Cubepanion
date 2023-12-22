package org.cubepanion.core.events;

import net.labymod.api.event.Event;
import org.cubepanion.core.utils.CubeGame;

public class GameWinEvent implements Event {

  private final CubeGame game;

  private final long gameStartTime;

  public GameWinEvent(CubeGame game, long gameStartTime) {
    this.game = game;
    this.gameStartTime = gameStartTime;
  }

  public CubeGame getGame() {
    return game;
  }

  public long getGameStartTime() {
    return gameStartTime;
  }

  public long getGameDuration() {
    return System.currentTimeMillis() - gameStartTime;
  }

}
