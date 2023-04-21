package org.cubepanion.core.utils;

public enum Leaderboard {
  TEAM_EGGWARS("Team EggWars"),
  TEAM_EGGWARS_SEASON_2("Team EggWars Season 2"),
  SOLO_LUCKYISLANDS("Lucky Islands"),
  SOLO_SKYWARS("Solo SkyWars"),
  FFA("Free For All"),
  PARKOUR("Parkour"),
  NONE("")
  ;


  private final String string;

  Leaderboard(String s) {
    this.string = s;
  }

  public String getString() {
    return string;
  }

  public static Leaderboard stringToLeaderboard(String s) {
    switch (s) {
      case "Team EggWars", "EggWars" -> {
        return Leaderboard.TEAM_EGGWARS;
      }
      case "EggWars Season 2" -> {
        return Leaderboard.TEAM_EGGWARS_SEASON_2;
      }
      case "Solo SkyWars", "SkyWars" -> {
        return Leaderboard.SOLO_SKYWARS;
      }
      case "Lucky Islands" -> {
        return Leaderboard.SOLO_LUCKYISLANDS;
      }
      case "Free For All", "FFA" -> {
        return Leaderboard.FFA;
      }
      case "Parkour" -> {
        return Leaderboard.PARKOUR;
      }
      default -> {
        return Leaderboard.NONE;
      }
    }
  }
}
