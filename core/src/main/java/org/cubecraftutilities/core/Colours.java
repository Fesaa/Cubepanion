package org.cubecraftutilities.core;

import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;

public class Colours {

  public static TextColor Title = NamedTextColor.GOLD;
  public static TextColor Primary = NamedTextColor.AQUA;
  public static TextColor Secondary = NamedTextColor.WHITE;
  public static TextColor Error = NamedTextColor.RED;
  public static TextColor Hover = NamedTextColor.GREEN;


  public static TextColor pingColour(int ping) {
    if (ping < 50) {
      return NamedTextColor.GREEN;
    } else if (ping < 100) {
      return NamedTextColor.DARK_GREEN;
    } else if (ping < 150) {
      return NamedTextColor.GOLD;
    } else if (ping < 200) {
      return NamedTextColor.RED;
    } else {
      return NamedTextColor.DARK_RED;
    }
  }

}
