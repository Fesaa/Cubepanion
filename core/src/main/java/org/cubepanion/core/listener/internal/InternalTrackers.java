package org.cubepanion.core.listener.internal;

import org.cubepanion.core.Cubepanion;

public class InternalTrackers {

  public static void register(Cubepanion addon) {
    addon.registerCubepanionListener(new TeamColour());
    addon.registerCubepanionListener(new GameEvents());
    addon.registerCubepanionListener(new Stats(addon));
    addon.registerCubepanionListener(new Party(addon));
    addon.registerCubepanionListener(new PlayerInfo(addon));
    addon.registerCubepanionListener(new ScoreboardListener(addon));
    addon.registerCubepanionListener(new ServerNavigation(addon));
  }

}
