package art.ameliah.laby.addons.cubepanion.core.listener.games;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.events.GameUpdateEvent;
import art.ameliah.laby.addons.cubepanion.core.weave.GameMapAPI;
import net.labymod.api.event.Subscribe;

public class MapLayout {

  @Subscribe
  public void onGameUpdate(GameUpdateEvent e) {
    if (!GameMapAPI.getInstance().hasMaps(e.getDestination())) {
      return;
    }

    if (e.isPreLobby()) {
      return;
    }

    if (!Cubepanion.get().configuration().getGameMapInfoSubConfig().isEnabled().get()) {
      return;
    }

    Cubepanion.get().getManager().getGameMapInfoManager().doGameMapLayout();
  }

}
