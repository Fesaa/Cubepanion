package org.cubepanion.core.utils;

public enum CubeGame {
  TEAM_EGGWARS("Team EggWars"),
  SOLO_LUCKYISLANDS("Lucky Islands"),
  SOLO_SKYWARS("Solo SkyWars"),
  FFA("Free For All"),
  SIMPLE_PARKOUR("Simple Parkour"),
  EASY_PARKOUR("Easy Parkour"),
  MEDIUM_PARKOUR("Medium Parkour"),
  HARD_PARKOUR("Hard Parkour"),
  PARKOUR("Parkour"),
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
        || e.equals(CubeGame.EASY_PARKOUR)
        || e.equals(CubeGame.SIMPLE_PARKOUR)
        || e.equals(CubeGame.PARKOUR);
  }

  public static CubeGame stringToGame(String s) {
      switch (s.toLowerCase().replace("_", " ")) {
          case "skyblock" -> {
              return CubeGame.SKYBLOCK;
          }
          case "team eggwars", "eggwars" -> {
              return CubeGame.TEAM_EGGWARS;
          }
          case "solo skywars", "skywars" -> {
              return CubeGame.SOLO_SKYWARS;
          }
          case "lucky islands", "solo lucky islands" -> {
              return CubeGame.SOLO_LUCKYISLANDS;
          }
          case "free for all", "ffa" -> {
              return CubeGame.FFA;
          }
          case "simple parkour" -> {
              return CubeGame.SIMPLE_PARKOUR;
          }
          case "easy parkour" -> {
              return CubeGame.EASY_PARKOUR;
          }
          case "medium parkour" -> {
              return CubeGame.MEDIUM_PARKOUR;
          }
          case "hard parkour" -> {
              return CubeGame.HARD_PARKOUR;
          }
          case "parkour" -> {
            return CubeGame.PARKOUR;
          }
          case "cubecraft" -> {
              return CubeGame.LOBBY;
          }
          default -> {
              return CubeGame.NONE;
          }
      }
  }
}
