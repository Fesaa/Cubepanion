package org.cubepanion.core.utils;

import art.ameliah.libs.weave.ChestAPI.ChestLocation;
import art.ameliah.libs.weave.WeaveException;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import org.cubepanion.core.Cubepanion;

public class Utils {

  public static Component chestLocationsComponent(ChestLocation loc) {
    return Component.text("Found a chest @ ", NamedTextColor.GREEN)
        .append(Component.text(String.format("%s, %s, %s", loc.x(), loc.y(), loc.z()),
            NamedTextColor.GRAY));
  }

  public static void handleResultError(Class<?> origin, Cubepanion addon, WeaveException e,
      String msg, String keyError, String key) {
    if (addon.configuration().getDebug().get()) {
      LOGGER.info(origin, e, msg);
    }
    if (addon.configuration().getLeaderboardAPIConfig().getErrorInfo().get()) {
      addon.displayMessage(
          Component.translatable(keyError, Component.text(e.getMessage())).color(Colours.Error));
    } else {
      addon.displayMessage(Component.translatable(key).color(Colours.Error));
    }
  }

}
