package art.ameliah.laby.addons.cubepanion.core.managers;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.events.CubeJoinEvent;
import art.ameliah.laby.addons.cubepanion.core.events.GameUpdateEvent;
import art.ameliah.laby.addons.cubepanion.core.events.RequestEvent;
import art.ameliah.laby.addons.cubepanion.core.managers.submanagers.DurabilityManager;
import art.ameliah.laby.addons.cubepanion.core.managers.submanagers.GameMapInfoManager;
import art.ameliah.laby.addons.cubepanion.core.managers.submanagers.FireballManager;
import art.ameliah.laby.addons.cubepanion.core.managers.submanagers.PartyManager;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import art.ameliah.laby.addons.cubepanion.core.weave.ChestAPI;
import art.ameliah.laby.addons.cubepanion.core.weave.GameMapAPI;
import art.ameliah.laby.addons.cubepanion.core.weave.LeaderboardAPI;
import net.labymod.api.Laby;

public class CubepanionManager implements Manager {
  // Sub Managers

  private final PartyManager partyManager;
  private final GameMapInfoManager gameMapInfoManager;
  private final DurabilityManager durabilityManager;
  private final FireballManager fireballManager;

  // Own fields

  private String serverIP;
  private boolean devServer;
  private CubeGame division;
  private CubeGame lastDivision;
  private String mapName;
  private String lastMapName;
  private String teamColour;
  private String rankString;
  private String serverID;
  private String lastServerID;

  private boolean eliminated;
  private boolean inPreLobby;
  private boolean won;

  private long gameStartTime;


  public CubepanionManager(Cubepanion addon) {
    this.partyManager = new PartyManager();
    this.gameMapInfoManager = new GameMapInfoManager(addon);
    this.durabilityManager = new DurabilityManager();
    this.fireballManager = new FireballManager();

    this.serverIP = "";
    this.devServer = false;
    this.division = CubeGame.NONE;
    this.lastDivision = CubeGame.NONE;
    this.mapName = "";
    this.lastMapName = "";
    this.teamColour = "";
    this.rankString = "";
    this.serverID = "";
    this.lastServerID = "";

    this.eliminated = false;
    this.inPreLobby = false;
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

  public void reset() {
    this.serverIP = "";
    this.devServer = false;
    this.lastDivision = CubeGame.NONE;
    this.division = CubeGame.NONE;
    this.teamColour = "";
    this.mapName = "";
    this.lastMapName = "";
    this.rankString = "";
    this.serverID = "";
    this.lastServerID = "";

    this.eliminated = false;
    this.inPreLobby = false;
    this.won = false;

    this.gameStartTime = -1;

    this.partyManager.reset();
    this.durabilityManager.reset();
    this.fireballManager.reset();
  }

  public void onCubeJoin() {
    Laby.fireEvent(new CubeJoinEvent());
    this.serverIP = "play.cubecraft.net";
    this.division = CubeGame.LOBBY;
    this.lastDivision = this.division;
    this.mapName = "Lobby";
    this.lastMapName = this.mapName;
    this.teamColour = "";
    this.rankString = "";
    this.serverID = "";
    this.updateRankString();

    this.eliminated = false;
    this.inPreLobby = true;

    this.gameStartTime = -1;

    this.partyManager.reset();

    LeaderboardAPI.getInstance().loadLeaderboards();
    GameMapAPI.getInstance().loadMaps();
    ChestAPI.getInstance().loadChestLocations();
    ChestAPI.getInstance().loadSeason();
  }

  public boolean isDevServer() {
    return devServer;
  }

  public void setDevServer(boolean devServer) {
    this.devServer = devServer;
  }

  public void onServerSwitch() {
    this.eliminated = false;
    this.inPreLobby = true;
    this.gameStartTime = -1;
    this.won = false;
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

  public boolean isInPreLobby() {
    return inPreLobby;
  }

  public void setInPreLobby(boolean inPreLobby) {
    this.inPreLobby = inPreLobby;
  }

  public boolean hasLost() {
    return !won;
  }

  public void setWon(boolean won) {
    this.won = won;
  }

  public CubeGame getDivision() {
    return division;
  }

  public void setDivision(CubeGame division) {
    this.lastDivision = this.division;
    this.division = division;

    if (this.division.equals(CubeGame.SKYBLOCK)
        || this.division.equals(CubeGame.FFA)
        || CubeGame.isParkour(this.division)
        || this.division.equals(CubeGame.LOBBY)) {
      this.inPreLobby = false;
      this.gameStartTime = System.currentTimeMillis();
    }
  }

  public boolean isPlaying(CubeGame game) {
    return division.equals(game) && !inPreLobby;
  }

  public CubeGame getLastDivision() {
    return lastDivision;
  }

  public String getMapName() {
    return mapName;
  }

  public void setMapName(String mapName) {
    this.lastMapName = this.mapName;
    this.mapName = mapName;
  }

  public String getLastMapName() {
    return lastMapName;
  }

  public String getServerIP() {
    return serverIP;
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

  public void setGameStartTime(long gameStartTime) {
    this.gameStartTime = gameStartTime;
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

  public FireballManager getFireballManager() {
    return fireballManager;
  }

  public String getServerID() {
    return serverID;
  }

  public void setServerID(String serverID) {
    this.lastServerID = this.serverID;
    this.serverID = serverID;
    GameUpdateEvent e = new GameUpdateEvent(
        this.lastDivision,
        this.division,
        this.inPreLobby,
        true);
    Laby.fireEvent(e);
  }

  public String getLastServerID() {
    return lastServerID;
  }
}
