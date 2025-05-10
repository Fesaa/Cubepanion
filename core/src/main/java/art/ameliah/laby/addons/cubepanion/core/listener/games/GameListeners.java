package art.ameliah.laby.addons.cubepanion.core.listener.games;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;

public class GameListeners {

  public static void register(Cubepanion addon) {
    if (addon.getVotingLink() != null) {
      addon.registerCubepanionListener(new AutoVote(addon, addon.getVotingLink()));
    }
    if (addon.getFunctionLink() != null) {
      addon.registerCubepanionListener(new Cooldowns(addon, addon.getFunctionLink()));
    }
    addon.registerCubepanionListener(new MapLayout());
    addon.registerCubepanionListener(new AutoGG());
    addon.registerCubepanionListener(new ClientPlayerSpawnProtection(addon));
  }

}
