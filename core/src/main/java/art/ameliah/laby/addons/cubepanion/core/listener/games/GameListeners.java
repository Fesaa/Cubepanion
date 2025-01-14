package art.ameliah.laby.addons.cubepanion.core.listener.games;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;

public class GameListeners {

  public static void register(Cubepanion addon) {
    addon.registerCubepanionListener(new AutoVote(addon));
    if (addon.getFunctionLink() != null) {
      addon.registerCubepanionListener(new Cooldowns(addon, addon.getFunctionLink()));
    }
    addon.registerCubepanionListener(new MapLayout());
    addon.registerCubepanionListener(new AutoGG());
    addon.registerCubepanionListener(new ClientPlayerSpawnProtection(addon));
    addon.registerCubepanionListener(new Stats(addon));
  }

}
