package org.cubepanion.core.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// ALSO ADD IN CubeGame#stringToGame !!!
public enum CubeGame {
  TEAM_EGGWARS("Team EggWars", true, "https://forums.cubecraftcdn.com/xenforo/serve/styles/cubecraft/cubecraft/minigames/node-icons/eggwars.png"),
  SOLO_LUCKYISLANDS("Lucky Islands", true, "https://forums.cubecraftcdn.com/xenforo/serve/styles/cubecraft/cubecraft/minigames/node-icons/lucky-islands.png"),
  SOLO_SKYWARS("Solo SkyWars", true, "https://forums.cubecraftcdn.com/xenforo/serve/styles/cubecraft/cubecraft/minigames/node-icons/skywars.png"),
  FFA("Free For All", true, "https://forums.cubecraftcdn.com/xenforo/serve/styles/cubecraft/cubecraft/minigames/node-icons/pvp.png"),
  SIMPLE_PARKOUR("Simple Parkour", false, "https://forums.cubecraftcdn.com/xenforo/serve/styles/cubecraft/cubecraft/minigames/node-icons/parkour.png"),
  EASY_PARKOUR("Easy Parkour", false, "https://forums.cubecraftcdn.com/xenforo/serve/styles/cubecraft/cubecraft/minigames/node-icons/parkour.png"),
  MEDIUM_PARKOUR("Medium Parkour", false, "https://forums.cubecraftcdn.com/xenforo/serve/styles/cubecraft/cubecraft/minigames/node-icons/parkour.png"),
  HARD_PARKOUR("Hard Parkour", false, "https://forums.cubecraftcdn.com/xenforo/serve/styles/cubecraft/cubecraft/minigames/node-icons/parkour.png"),
  PARKOUR("Parkour", false, "https://forums.cubecraftcdn.com/xenforo/serve/styles/cubecraft/cubecraft/minigames/node-icons/parkour.png"),
  SKYBLOCK("Skyblock", false, "https://forums.cubecraftcdn.com/xenforo/serve/styles/cubecraft/cubecraft/minigames/node-icons/skyblock.png"),
  SNOWMAN_SURVIVAL("Snowman Survival", true, null),
  LOBBY("Main Lobby", false, null),
  NONE("", false, null);


  private final String string;
  private final boolean shouldTrack;
  private final String url;

  CubeGame(String s, boolean shouldTrack, @Nullable String assetURL) {
    this.string = s;
    this.shouldTrack = shouldTrack;
    this.url = assetURL;
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
      case "snowman survival" -> {
        return CubeGame.SNOWMAN_SURVIVAL;
      }
      case "cubecraft" -> {
        return CubeGame.LOBBY;
      }
      default -> {
        return CubeGame.NONE;
      }
    }
  }

  public boolean shouldTrack() {
    return shouldTrack;
  }

  public String getString() {
    return string;
  }

  public @NotNull String getUrl() {
    return url == null
        ? "https://forums.cubecraftcdn.com/xenforo/data/avatars/o/307/307406.jpg?1591095808"
        : url;
  }
}
