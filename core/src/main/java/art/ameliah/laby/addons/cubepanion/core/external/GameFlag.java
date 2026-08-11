package art.ameliah.laby.addons.cubepanion.core.external;

public enum GameFlag {
  RESPAWN_TAGS(1),
  NO_PRE_GAME_STATE(1 << 1),
  LOBBY(1 << 2),
  GAME(1 << 3),
  DISCORD_RPC_PLAYER_TRACKING(1 << 4),
  DONT_DROP_TOOLS(1 << 5),
  IGNORE_SCOREBOARD_UPDATES(1 << 6),
  ALTERNATE_MAP_TRACKER(1 << 7),
  HAS_RESPAWNS(1 << 8),
  IN_GAME_AFTER_ELIMINATION(1 << 9),
  COOLDOWNS(1 << 10),
  MINI_GAME(1 << 11),
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
