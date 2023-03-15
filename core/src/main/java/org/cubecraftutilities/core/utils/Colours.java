package org.cubecraftutilities.core.utils;

import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;

public class Colours {

  public static final TextColor Title = NamedTextColor.GOLD;
  public static final TextColor Primary = NamedTextColor.AQUA;
  public static final TextColor Secondary = NamedTextColor.WHITE;
  public static final TextColor Error = NamedTextColor.RED;
  public static final TextColor Hover = NamedTextColor.GREEN;


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

  public static TextColor colourToNamedTextColor(String colour) {
    switch (colour) {
      case "black": {return NamedTextColor.BLACK;}
      case "dark_blue": {return NamedTextColor.DARK_BLUE;}
      case "dark_green": {return NamedTextColor.DARK_GREEN;}
      case "dark_aqua": {return NamedTextColor.DARK_AQUA;}
      case "dark_red": {return NamedTextColor.DARK_RED;}
      case "dark_purple": {return NamedTextColor.DARK_PURPLE;}
      case "gold": {return NamedTextColor.GOLD;}
      case "gray": {return NamedTextColor.GRAY;}
      case "dark_gray": {return NamedTextColor.DARK_GRAY;}
      case "blue": {return NamedTextColor.BLUE;}
      case "green": {return NamedTextColor.GREEN;}
      case "aqua": {return NamedTextColor.AQUA;}
      case "red": {return NamedTextColor.RED;}
      case "light_purple": {return NamedTextColor.LIGHT_PURPLE;}
      case "yellow": {return NamedTextColor.YELLOW;}
      default: {return NamedTextColor.WHITE;}
    }
  }

  public static String colourToUnicode(String colour) {
    switch (colour) {
      case "black": {return "§0";}
      case "dark_blue": {return "§1";}
      case "dark_green": {return "§2";}
      case "dark_aqua": {return "§3";}
      case "dark_red": {return "§4";}
      case "dark_purple": {return "§5";}
      case "gold": {return "§6";}
      case "gray": {return "§7";}
      case "dark_gray": {return "§8";}
      case "blue": {return "§9";}
      case "green": {return "§a";}
      case "aqua": {return "§b";}
      case "red": {return "§c";}
      case "light_purple": {return "§d";}
      case "yellow": {return "§e";}
      case "white": {return "§f";}
    }
    return "";
  }

  public static String colourToCubeColour(String colour) {
    switch (colour) {
      case "black": {return "&0";}
      case "dark_blue": {return "&1";}
      case "dark_green": {return "&2";}
      case "dark_aqua": {return "&3";}
      case "dark_red": {return "&4";}
      case "dark_purple": {return "&5";}
      case "gold": {return "&6";}
      case "gray": {return "&7";}
      case "dark_gray": {return "&8";}
      case "blue": {return "&9";}
      case "green": {return "&a";}
      case "aqua": {return "&b";}
      case "red": {return "&c";}
      case "light_purple": {return "&d";}
      case "yellow": {return "&e";}
      case "white": {return "&f";}
    }
    return "";
  }

  public static String colourToCubeColourString(String colour) {
    switch (colour) {
      case "black":
      case "gray":
      case "dark_gray": {return "Black";}
      case "dark_blue":
      case "blue": {return "Blue";}
      case "dark_green":
      case "green": {return "Green";}
      case "dark_red":
      case "red": {return "Red";}
      case "dark_aqua": {return "Cyan";}
      case "dark_purple": {return "Purple";}
      case "gold": {return "Orange";}
      case "aqua": {return "Light Blue";}
      case "light_purple": {return "Pink";}
      case "yellow": {return "Yellow";}
      case "white": {return "White";}
    }
    return "";
  }

}
