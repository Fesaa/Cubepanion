package art.ameliah.laby.addons.cubepanion.core.external;

import java.util.List;

public record Game(int id, String name, String displayName, List<String> aliases, boolean active, String scoreType, boolean shouldTrack) {

  public static Game UNKNOWN = new Game(0, "unknown", "Unknown", List.of(), false, "Unknown", false);

  public static Game LOBBY = new Game(0, "main_lobby", "Main Lobby", List.of(),false, "Main Lobby", false);

}
