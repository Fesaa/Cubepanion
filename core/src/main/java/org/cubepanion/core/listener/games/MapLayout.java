package org.cubepanion.core.listener.games;

import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.events.GameUpdateEvent;
import org.cubepanion.core.utils.CubeGame;

public class MapLayout {

  public void onGameUpdate(GameUpdateEvent e) {
    if (!e.getDestination().equals(CubeGame.TEAM_EGGWARS)) {
      return;
    }

    if (e.isPreLobby()) {
      return;
    }

    if (!Cubepanion.get().configuration().getEggWarsMapInfoSubConfig().isEnabled().get()) {
      return;
    }

    Cubepanion.get().getManager().getEggWarsMapInfoManager().doEggWarsMapLayout();
  }

}
