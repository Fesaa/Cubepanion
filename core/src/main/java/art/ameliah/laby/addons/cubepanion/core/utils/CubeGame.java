package art.ameliah.laby.addons.cubepanion.core.utils;


// TODO: Bonk this enum and load all info from API. We can used named constants for checking
// ALSO ADD IN CubeGame#stringToGame !!!
public enum CubeGame {
  TEAM_EGGWARS("Team EggWars", true),
  SOLO_LUCKYISLANDS("Solo Lucky Islands", true),
  TEAM_LUCKY_ISLANDS("Team Lucky Islands", true),
  SOLO_SKYWARS("Solo SkyWars", true),
  FFA("Free For All", false),
  SIMPLE_PARKOUR("Simple Parkour", false),
  EASY_PARKOUR("Easy Parkour", false),
  MEDIUM_PARKOUR("Medium Parkour", false),
  HARD_PARKOUR("Hard Parkour", false),
  PARKOUR("Parkour", false),
  SKYBLOCK("Skyblock", false),
  SNOWMAN_SURVIVAL("Snowman Survival", true),
  LOBBY("Main Lobby", false),
  PILLARS_OF_FORTUNE("Pillars of Fortune", true),
  BEDWARS("BedWars", true),
  ENDER("Ender", true),
  DISASTERS("Disasters", true),
  LUCKY_PILLARS("Lucky Pillars", true),
  MOB_WHO("Mob Who", true),
  NONE("", false);


  private final String string;
  private final boolean isMiniGame;

  CubeGame(String s, boolean isMiniGame) {
    this.string = s;
    this.isMiniGame = isMiniGame;
  }

  public static boolean isParkour(CubeGame e) {
    return e.equals(CubeGame.HARD_PARKOUR)
        || e.equals(CubeGame.MEDIUM_PARKOUR)
        || e.equals(CubeGame.EASY_PARKOUR)
        || e.equals(CubeGame.SIMPLE_PARKOUR)
        || e.equals(CubeGame.PARKOUR);
  }

  public boolean isMiniGame() {
    return isMiniGame;
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
      case "lucky pillars" -> {
        return CubeGame.LUCKY_PILLARS;
      }
      case "mob who" -> {
        return CubeGame.MOB_WHO;
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
