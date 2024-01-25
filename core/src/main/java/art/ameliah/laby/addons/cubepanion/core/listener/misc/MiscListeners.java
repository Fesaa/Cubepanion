package art.ameliah.laby.addons.cubepanion.core.listener.misc;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;

public class MiscListeners {

  public static void register(Cubepanion addon) {
    if (addon.getChestFinderLink() != null) {
      addon.registerCubepanionListener(new ChestFinder(addon, addon.getChestFinderLink()));
    }

    addon.registerCubepanionListener(new RankTag());
    addon.registerCubepanionListener(new DiscordRPC(addon));
  }

}
