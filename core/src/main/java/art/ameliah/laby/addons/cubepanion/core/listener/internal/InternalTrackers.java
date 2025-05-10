package art.ameliah.laby.addons.cubepanion.core.listener.internal;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.versionlinkers.FunctionLink;

public class InternalTrackers {

  public static void register(Cubepanion addon, FunctionLink functionLink) {
    addon.registerCubepanionListener(new TeamColour());
    addon.registerCubepanionListener(new GameEvents());
    addon.registerCubepanionListener(new Stats(addon));
    addon.registerCubepanionListener(new Party(addon));
    addon.registerCubepanionListener(new PlayerInfo(addon));
    addon.registerCubepanionListener(new ScoreboardListener(addon));
    addon.registerCubepanionListener(new ServerNavigation(addon));
    addon.registerCubepanionListener(new SessionTracker());
    addon.registerCubepanionListener(new LeaderboardTracker(addon));
    if (functionLink != null) {
      addon.registerCubepanionListener(new PerkTracker(addon, functionLink));
    }
  }

}
