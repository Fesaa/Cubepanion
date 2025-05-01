package art.ameliah.laby.addons.cubepanion.core.weave;

/**
 * @param name        internal name
 * @param displayName display name
 * @param active      if the leaderboard of this game is active (and can be submitted to)
 */
public record APIGame(String name, String displayName, boolean active, String scoreType) {

  public static APIGame UNKNOWN = new APIGame("unknown", "Unknown", false, "Unknown");

  public static APIGame LOBBY = new APIGame("main_lobby", "Main Lobby", false, "Main Lobby");

}
