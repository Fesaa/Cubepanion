package art.ameliah.laby.addons.cubepanion.core.listener.games;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.events.GameUpdateEvent;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import net.labymod.api.event.Subscribe;

public class MapLayout {

  @Subscribe
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
