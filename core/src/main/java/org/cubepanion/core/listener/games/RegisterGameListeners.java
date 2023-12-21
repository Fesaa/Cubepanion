package org.cubepanion.core.listener.games;

import org.cubepanion.core.Cubepanion;

public class RegisterGameListeners {

  public static void register(Cubepanion addon) {
    addon.registerCubepanionListener(new AutoVote());
    addon.registerCubepanionListener(new FireballCooldown(addon, addon.getFunctionLink()));
  }

}
