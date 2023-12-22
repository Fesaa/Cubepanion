package org.cubepanion.core.listener.misc;

import org.cubepanion.core.Cubepanion;

public class MiscListeners {

  public static void register(Cubepanion addon) {
    if (addon.getChestFinderLink() != null) {
      addon.registerCubepanionListener(new ChestFinder(addon, addon.getChestFinderLink()));
    }
  }

}
