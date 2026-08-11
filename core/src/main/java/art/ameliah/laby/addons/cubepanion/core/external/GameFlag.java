package art.ameliah.laby.addons.cubepanion.core.external;

public enum GameFlag {
  RESPAWN_TAGS(1),
  NO_PRE_GAME_STATE(1 << 1),
  LOBBY(1 << 2),
  DISCORD_RPC_PLAYER_TRACKING(1 << 3),
  DONT_DROP_TOOLS(1 << 4),
  IGNORE_SCOREBOARD_UPDATES(1 << 5),
  ALTERNATE_MAP_TRACKER(1 << 6),
  HAS_RESPAWNS(1 << 7),
  IN_GAME_AFTER_ELIMINATION(1 << 8),
  COOLDOWNS(1 << 9),
  MINI_GAME(1 << 10),
  ;

  private final int bit;

  GameFlag(int bit) {
    this.bit = bit;
  }

  public boolean isSetIn(int flags) {
    return (flags & bit) != 0;
  }

  public int I() {
    return bit;
  }

}
