package org.cubepanion.core.utils;

public enum CubeGame {
  TEAM_EGGWARS("Team EggWars"),
  SOLO_LUCKYISLANDS("Lucky Islands"),
  SOLO_SKYWARS("Solo SkyWars"),
  FFA("Free For All"),
  SIMPLE_PARKOUR("Simple Parkour"),
  NORMAL_PARKOUR("Normal Parkour"),
  MEDIUM_PARKOUR("Medium Parkour"),
  HARD_PARKOUR("Hard Parkour"),
  SKYBLOCK("Skyblock"),
  LOBBY("Main Lobby"),
  NONE("")
  ;


  private final String string;

   CubeGame(String s) {
    this.string = s;
  }

  public String getString() {
    return string;
  }

  public static boolean isParkour(CubeGame e) {
    return e.equals(CubeGame.HARD_PARKOUR)
        || e.equals(CubeGame.MEDIUM_PARKOUR)
        || e.equals(CubeGame.NORMAL_PARKOUR)
        || e.equals(CubeGame.SIMPLE_PARKOUR);
  }

  public static CubeGame stringToGame(String s) {
    switch (s) {
      case "Skyblock": {
        return CubeGame.SKYBLOCK;
      }
      case "Team EggWars": {
        return CubeGame.TEAM_EGGWARS;
      }
      case "Solo SkyWars": {
        return CubeGame.SOLO_SKYWARS;
      }
      case "Lucky Islands": {
        return CubeGame.SOLO_LUCKYISLANDS;
      }
      case "Free For All": {
        return CubeGame.FFA;
      }
      case "Simple Parkour":
        return CubeGame.SIMPLE_PARKOUR;
      case "Normal Parkour":
        return CubeGame.NORMAL_PARKOUR;
      case "Medium Parkour":
        return CubeGame.MEDIUM_PARKOUR;
      case "Hard Parkour": {
        return CubeGame.HARD_PARKOUR;
      }
      case "CubeCraft": {
        return CubeGame.LOBBY;
      }
      default: {
        return CubeGame.NONE;
      }
    }
  }
}
