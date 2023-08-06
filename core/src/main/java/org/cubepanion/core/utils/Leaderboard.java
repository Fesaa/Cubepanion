package org.cubepanion.core.utils;

public enum Leaderboard {
  TEAM_EGGWARS("Team EggWars"),
  TEAM_EGGWARS_SEASON_2("Team EggWars Season 2"),
  SOLO_LUCKYISLANDS("Lucky Islands"),
  SOLO_SKYWARS("Solo SkyWars"),
  FFA("Free For All"),
  NONE(""),
  PARKOUR("Parkour");


  private final String string;

  Leaderboard(String s) {
    this.string = s;
  }

  public static Leaderboard stringToLeaderboard(String s) {
    switch (s.toLowerCase()) {
      case "team eggwars", "eggwars", "tew", "ew" -> {
        return Leaderboard.TEAM_EGGWARS;
      }
      case "eggwars season 2", "ew2", "tew2" -> {
        return Leaderboard.TEAM_EGGWARS_SEASON_2;
      }
      case "solo skywars", "skywars", "sw" -> {
        return Leaderboard.SOLO_SKYWARS;
      }
      case "lucky islands", "li" -> {
        return Leaderboard.SOLO_LUCKYISLANDS;
      }
      case "free for all", "ffa" -> {
        return Leaderboard.FFA;
      }
      case "parkour" -> {
        return Leaderboard.PARKOUR;
      }
      default -> {
        return Leaderboard.NONE;
      }
    }
  }

  public String getString() {
    return string;
  }
}
