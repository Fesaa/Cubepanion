package org.cubepanion.core.utils;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;

public record ChestLocation(int x, int y, int z) {

  public Component component() {
    return Component.text("Found a chest @", NamedTextColor.GREEN)
        .append(Component.text(String.format("%s, %s, %s", x, y, z), NamedTextColor.GRAY));
  }

}
