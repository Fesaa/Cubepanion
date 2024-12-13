package art.ameliah.laby.addons.cubepanion.core.listener.games;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.events.GameStartEvent;
import net.labymod.api.event.Subscribe;

public class MapLayout {

  @Subscribe
  public void onGameUpdate(GameStartEvent e) {
    if (!Cubepanion.get().configuration().getGameMapInfoSubConfig().isEnabled().get()) {
      return;
    }

    Cubepanion.get().getManager().getGameMapInfoManager().doGameMapLayout();
  }

}
