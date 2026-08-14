package art.ameliah.laby.addons.cubepanion.core.managers;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.events.CubeJoinEvent;
import art.ameliah.laby.addons.cubepanion.core.events.GameEndEvent;
import art.ameliah.laby.addons.cubepanion.core.events.GameJoinEvent;
import art.ameliah.laby.addons.cubepanion.core.events.GameStartEvent;
import art.ameliah.laby.addons.cubepanion.core.events.RequestEvent;
import art.ameliah.laby.addons.cubepanion.core.events.RequestEvent.RequestType;
import art.ameliah.laby.addons.cubepanion.core.external.CubepanionAPI;
import art.ameliah.laby.addons.cubepanion.core.external.Game;
import art.ameliah.laby.addons.cubepanion.core.external.GameFlag;
import art.ameliah.laby.addons.cubepanion.core.managers.submanagers.CooldownManager;
import art.ameliah.laby.addons.cubepanion.core.managers.submanagers.DurabilityManager;
import art.ameliah.laby.addons.cubepanion.core.managers.submanagers.GameMapInfoManager;
import art.ameliah.laby.addons.cubepanion.core.managers.submanagers.PartyManager;
import net.labymod.api.Laby;
import net.labymod.api.util.logging.Logging;

public class CubepanionManager implements Manager {

  private static final Logging log = Logging.create(Cubepanion.class.getSimpleName());

  private final PartyManager partyManager;
  private final GameMapInfoManager gameMapInfoManager;
  private final DurabilityManager durabilityManager;
  private final CooldownManager cooldownManager;

  private String serverIP;
  private boolean devServer;

  private Game game;
  private Game lastGame;

  private String mapName;
  private String lastMapName;

  private String teamColour;
  private String rankString;

  private String serverID;
  private String lastServerID;

  private boolean eliminated;
  private boolean won;

  /**
   * If we're currently in a pre lobby or in cages
   */
  private boolean inPreGameState;
  private long gameStartTime;


  public CubepanionManager(Cubepanion addon) {
    this.partyManager = new PartyManager();
    this.gameMapInfoManager = new GameMapInfoManager(addon);
    this.durabilityManager = new DurabilityManager();
    this.cooldownManager = new CooldownManager();

    this.serverIP = "";
    this.devServer = false;
    this.game = Game.UNKNOWN;
    this.lastGame = Game.UNKNOWN;
    this.mapName = "";
    this.lastMapName = "";
    this.teamColour = "";
    this.rankString = "";
    this.serverID = "";
    this.lastServerID = "";

    this.eliminated = false;
    this.inPreGameState = false;
    this.won = false;

    this.gameStartTime = -1;
  }


  public PartyManager getPartyManager() {
    return this.partyManager;
  }

  public GameMapInfoManager getGameMapInfoManager() {
    return this.gameMapInfoManager;
  }

  public DurabilityManager getDurabilityManager() {
    return this.durabilityManager;
  }

  public CooldownManager getCooldownManager() {
    return cooldownManager;
  }

  public void reset() {
    this.serverIP = "";
    this.devServer = false;
    this.lastGame = Game.UNKNOWN;
    this.game = Game.UNKNOWN;
    this.teamColour = "";
    this.mapName = "";
    this.lastMapName = "";
    this.rankString = "";
    this.serverID = "";
    this.lastServerID = "";

    this.eliminated = false;
    this.inPreGameState = false;
    this.won = false;

    this.gameStartTime = -1;

    this.partyManager.reset();
    this.durabilityManager.reset();
    this.cooldownManager.reset();
  }

  public void onCubeJoin() {
    Laby.fireEvent(new CubeJoinEvent());
    this.serverIP = "play.cubecraft.net";
    this.game = Game.LOBBY;
    this.lastGame = this.game;
    this.mapName = "Lobby";
    this.lastMapName = this.mapName;
    this.teamColour = "";
    this.rankString = "";
    this.serverID = "";
    this.updateRankString();

    this.eliminated = false;
    this.inPreGameState = false;

    this.gameStartTime = -1;

    this.partyManager.reset();

    CubepanionAPI.I().loadInitialData();
  }

  public boolean isProd() {
    return !devServer;
  }

  public void setDevServer(boolean devServer) {
    this.devServer = devServer;
  }

  public boolean onCubeCraft() {
    return this.serverIP.equals("play.cubecraft.net");
  }

  public boolean isEliminated() {
    return eliminated;
  }

  public void setEliminated(boolean eliminated) {
    this.eliminated = eliminated;
  }

  public boolean isInPreGameState() {
    return inPreGameState;
  }

  public void onGameStart() {
    log.debug("Starting {}! PreLobby: {}", game, this.inPreGameState);
    this.inPreGameState = false;
    this.gameStartTime = System.currentTimeMillis();

    Laby.fireEvent(new GameStartEvent(this.game));
  }

  public boolean hasLost() {
    return !won;
  }

  public void setWon(boolean won) {
    this.won = won;
  }

  public Game getGame() {
    return game;
  }

  public void setGame(Game game) {
    if (game.hasPreLobby() && this.isInPreGameState()) {
      log.debug("{} has a pre lobby, ignoring update", game.displayName());
      return;
    }

    log.debug("Setting division to {} and firing join event", game);

    if (!this.isInPreGameState() && this.hasLost()) {
      log.debug("Ending game due to division switch");
      Laby.fireEvent(new GameEndEvent(this.game, false, true, this.gameStartTime));
    }

    this.lastGame = this.game;
    this.game = game;

    this.eliminated = false;
    this.inPreGameState = true;
    this.gameStartTime = -1;
    this.won = false;

    if (this.game.hasFlagEnabled(GameFlag.NO_PRE_GAME_STATE)) {
      this.inPreGameState = false;
      this.gameStartTime = System.currentTimeMillis();
    }

    Laby.fireEvent(new GameJoinEvent(this.lastGame, this.game, this.inPreGameState));
    Laby.fireEvent(new RequestEvent(RequestType.UPDATE_RPC));
  }

  public Game getLastGame() {
    return lastGame;
  }

  public String getMapName() {
    return mapName;
  }

  public void setMapName(String mapName) {
    if (this.mapName.equals(mapName)) return;

    this.lastMapName = this.mapName;
    this.mapName = mapName;
  }

  public String getLastMapName() {
    return lastMapName;
  }

  public String getTeamColour() {
    return teamColour;
  }

  public void setTeamColour(String teamColour) {
    this.teamColour = teamColour;
  }

  public long getGameStartTime() {
    return gameStartTime;
  }

  public String getRankString() {
    return rankString;
  }

  public void setRankString(String rankString) {
    this.rankString = rankString;
  }

  public void updateRankString() {
    Laby.fireEvent(new RequestEvent(RequestEvent.RequestType.RANK_TAG));
    Laby.labyAPI().minecraft().chatExecutor().chat("/who", false);
  }

  public String getServerID() {
    return serverID;
  }

  public void setServerID(String serverID) {
    this.lastServerID = this.serverID;
    this.serverID = serverID;
  }

  public String getLastServerID() {
    return lastServerID;
  }
}
