package art.ameliah.laby.addons.cubepanion.core.utils;

// ALSO ADD IN CubeGame#stringToGame !!!
public enum CubeGame {
  TEAM_EGGWARS("Team EggWars"),
  SOLO_LUCKYISLANDS("Solo Lucky Islands"),
  TEAM_LUCKY_ISLANDS("Team Lucky Islands"),
  SOLO_SKYWARS("Solo SkyWars"),
  FFA("Free For All"),
  SIMPLE_PARKOUR("Simple Parkour"),
  EASY_PARKOUR("Easy Parkour"),
  MEDIUM_PARKOUR("Medium Parkour"),
  HARD_PARKOUR("Hard Parkour"),
  PARKOUR("Parkour"),
  SKYBLOCK("Skyblock"),
  SNOWMAN_SURVIVAL("Snowman Survival"),
  LOBBY("Main Lobby"),
  PILLARS_OF_FORTUNE("Pillars of Fortune"),
  BEDWARS("BedWars"),
  ENDER("Ender"),
  DISASTERS("Disasters"),
  NONE("");


  private final String string;

  CubeGame(String s) {
    this.string = s;
  }

  public static boolean isParkour(CubeGame e) {
    return e.equals(CubeGame.HARD_PARKOUR)
        || e.equals(CubeGame.MEDIUM_PARKOUR)
        || e.equals(CubeGame.EASY_PARKOUR)
        || e.equals(CubeGame.SIMPLE_PARKOUR)
        || e.equals(CubeGame.PARKOUR);
  }

  public static CubeGame stringToGame(String s) {
    String target = s.toLowerCase().replace("_", " ");

    for (CubeGame g : CubeGame.values()) {
      if (g.string.equalsIgnoreCase(target)) {
        return g;
      }
    }

    // Fallback to manual
    switch (target) {
      case "skyblock" -> {
        return CubeGame.SKYBLOCK;
      }
      case "team eggwars", "eggwars" -> {
        return CubeGame.TEAM_EGGWARS;
      }
      case "solo skywars", "skywars" -> {
        return CubeGame.SOLO_SKYWARS;
      }
      case "solo lucky islands" -> {
        return CubeGame.SOLO_LUCKYISLANDS;
      }
      case "team lucky islands", "lucky islands" -> {
        return CubeGame.TEAM_LUCKY_ISLANDS;
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
      case "snowman survival" -> {
        return CubeGame.SNOWMAN_SURVIVAL;
      }
      case "cubecraft" -> {
        return CubeGame.LOBBY;
      }
      case "pillars of fortune" -> {
        return CubeGame.PILLARS_OF_FORTUNE;
      }
      case "team bedwars", "bedwars" -> {
        return CubeGame.BEDWARS;
      }
      case "ender" -> {
        return CubeGame.ENDER;
      }
      case "disasters" -> {
        return CubeGame.DISASTERS;
      }
      default -> {
        return CubeGame.NONE;
      }
    }
  }

  public String getString() {
    return string;
  }
}
