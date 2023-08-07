package org.cubepanion.core.utils;

import art.ameliah.libs.weave.ChestAPI.ChestLocation;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;

public class Utils {

  public static Component chestLocationsComponent(ChestLocation loc) {
    return Component.text("Found a chest @ ", NamedTextColor.GREEN)
        .append(Component.text(String.format("%s, %s, %s", loc.x(), loc.y(), loc.z()),
            NamedTextColor.GRAY));
  }

}
