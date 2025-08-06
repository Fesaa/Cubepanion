package art.ameliah.laby.addons.cubepanion.core.external;

import java.util.List;

/**
 *
 * @param id internal id
 * @param name name
 * @param displayName pretty name
 * @param aliases other ways of writing the game I.e. tew, li
 * @param active game currently playable
 * @param scoreType wins, medals, kills, etc
 * @param shouldTrack is the game is eligible for tracking by the stats tracker
 * @param hasPreLobby this is an actual pre lobby, as opposed to the cages also counting in manager
 */
public record Game(int id, String name, String displayName, List<String> aliases, boolean active, String scoreType, boolean shouldTrack, boolean hasPreLobby) {

  public static Game UNKNOWN = new Game(0, "unknown", "Unknown", List.of(), false, "Unknown", false, false);

  public static Game LOBBY = new Game(0, "main_lobby", "Main Lobby", List.of(),false, "Main Lobby", false, false);

}
