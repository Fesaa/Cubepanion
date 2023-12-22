package org.cubepanion.core.listener.internal;

import org.cubepanion.core.Cubepanion;

public class InternalTrackers {

  public static void register(Cubepanion addon) {
    addon.registerCubepanionListener(new TeamColour());
    addon.registerCubepanionListener(new GameEvents());
  }

}
