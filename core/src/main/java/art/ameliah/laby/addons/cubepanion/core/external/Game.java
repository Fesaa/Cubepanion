package art.ameliah.laby.addons.cubepanion.core.external;

import java.util.List;

public final class Game {
  private final int id;
  private final String name;
  private final String displayName;
  private final List<String> aliases;
  private final boolean active;
  private final String scoreType;
  private final boolean shouldTrack;
  private final boolean hasPreLobby;
  private final String icon;
  private final int enabledFlags;

  public Game(
      int id,
      String name,
      String displayName,
      List<String> aliases,
      boolean active,
      String scoreType,
      boolean shouldTrack,
      boolean hasPreLobby,
      String icon,
      int enabledFlags
  ) {
    this.id = id;
    this.name = name;
    this.displayName = displayName;
    this.aliases = List.copyOf(aliases);
    this.active = active;
    this.scoreType = scoreType;
    this.shouldTrack = shouldTrack;
    this.hasPreLobby = hasPreLobby;
    this.icon = icon;
    this.enabledFlags = enabledFlags;
  }

  public static final Game UNKNOWN =
      new Game(0, "unknown", "Unknown", List.of(), false, "Unknown", false, false, "", 0);

  public static final Game LOBBY =
      new Game(0, "main_lobby", "Main Lobby", List.of(), false, "Main Lobby", false, false, "", GameFlag.LOBBY.I());

  public int id() {
    return id;
  }

  public String name() {
    return name;
  }

  public String displayName() {
    return displayName;
  }

  public List<String> aliases() {
    return aliases;
  }

  public boolean active() {
    return active;
  }

  public String scoreType() {
    return scoreType;
  }

  public boolean shouldTrack() {
    return shouldTrack;
  }

  public boolean hasPreLobby() {
    return hasPreLobby;
  }

  public String icon() {
    return icon;
  }

  public boolean hasFlagEnabled(GameFlag flag) {
    return flag.isSetIn(enabledFlags);
  }

  @Override
  public String toString() {
    return "Game{" +
        "id=" + id +
        ", displayName='" + displayName + '\'' +
        ", hasPreLobby=" + hasPreLobby +
        ", enabledFlags=" + enabledFlags +
        '}';
  }
}
