package org.cubepanion.core.listener.games;

import org.cubepanion.core.Cubepanion;

public class GameListeners {

  public static void register(Cubepanion addon) {
    addon.registerCubepanionListener(new AutoVote());
    if (addon.getFunctionLink() != null) {
      addon.registerCubepanionListener(new FireballCooldown(addon, addon.getFunctionLink()));
    }
    addon.registerCubepanionListener(new VoteReminder(addon));
    addon.registerCubepanionListener(new MapLayout());
    addon.registerCubepanionListener(new AutoGG());
    addon.registerCubepanionListener(new ClientPlayerSpawnProtection(addon));
  }

}
