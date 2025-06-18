package art.ameliah.laby.addons.cubepanion.core.listener.games;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;

public class GameListeners {

  public static void register(Cubepanion addon) {
    var func = addon.getFunctionLink();

    if (func != null) {
      addon.registerCubepanionListener(new AutoVote(addon, func));
      addon.registerCubepanionListener(new Cooldowns(addon, func));
    }
    addon.registerCubepanionListener(new MapLayout());
    addon.registerCubepanionListener(new AutoGG());
    addon.registerCubepanionListener(new ClientPlayerSpawnProtection(addon));
    addon.registerCubepanionListener(new Stats(addon));
  }

}
