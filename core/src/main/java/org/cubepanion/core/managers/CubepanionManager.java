package org.cubepanion.core.managers;

import net.labymod.api.Laby;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.events.GameUpdateEvent;
import org.cubepanion.core.events.RequestEvent;
import org.cubepanion.core.managers.submanagers.DurabilityManager;
import org.cubepanion.core.managers.submanagers.EggWarsMapInfoManager;
import org.cubepanion.core.managers.submanagers.FireballManager;
import org.cubepanion.core.managers.submanagers.PartyManager;
import org.cubepanion.core.managers.submanagers.SpawnProtectionManager;
import org.cubepanion.core.utils.CubeGame;
import org.cubepanion.core.utils.eggwarsmaps.base.LoadedEggWarsMap;
import org.cubepanion.core.weave.ChestAPI;
import org.cubepanion.core.weave.EggWarsMapAPI;
import org.cubepanion.core.weave.LeaderboardAPI;
import org.jetbrains.annotations.Nullable;

public class CubepanionManager implements Manager {
  // Sub Managers

  private final PartyManager partyManager;
  private final EggWarsMapInfoManager eggWarsMapInfoManager;
  private final SpawnProtectionManager spawnProtectionManager;
  private final DurabilityManager durabilityManager;
  private final FireballManager fireballManager;

  // Own fields

  private String serverIP;
  private CubeGame division;
  private CubeGame lastDivision;
  private String mapName;
  private String teamColour;
  private String bungeecord;
  private String serverID;
  private String rankString;
  private LoadedEggWarsMap currentEggWarsMap;

  private boolean eliminated;
  private boolean inPreLobby;
  private boolean won;
  private boolean hasUpdatedAfterServerSwitch;

  private long gameStartTime;


  public CubepanionManager(Cubepanion addon) {
    this.partyManager = new PartyManager();
    this.eggWarsMapInfoManager = new EggWarsMapInfoManager(addon);
    this.spawnProtectionManager = new SpawnProtectionManager(addon);
    this.durabilityManager = new DurabilityManager();
    this.fireballManager = new FireballManager();

    this.serverIP = "";
    this.division = CubeGame.NONE;
    this.lastDivision = CubeGame.NONE;
    this.mapName = "";
    this.teamColour = "";
    this.bungeecord = "";
    this.serverID = "";
    this.rankString = "";
    this.currentEggWarsMap = null;

    this.eliminated = false;
    this.inPreLobby = false;
    this.won = false;
    this.hasUpdatedAfterServerSwitch = false;

    this.gameStartTime = -1;
  }


  public PartyManager getPartyManager() {
    return this.partyManager;
  }

  public EggWarsMapInfoManager getEggWarsMapInfoManager() {
    return this.eggWarsMapInfoManager;
  }

  public DurabilityManager getDurabilityManager() {
    return this.durabilityManager;
  }

  public SpawnProtectionManager getSpawnProtectionManager() {
    return spawnProtectionManager;
  }

  public void reset() {
    this.serverIP = "";
    this.lastDivision = CubeGame.NONE;
    this.division = CubeGame.NONE;
    this.teamColour = "";
    this.mapName = "";
    this.bungeecord = "";
    this.serverID = "";
    this.rankString = "";

    this.eliminated = false;
    this.inPreLobby = false;
    this.won = false;
    this.hasUpdatedAfterServerSwitch = false;

    this.gameStartTime = -1;

    this.partyManager.reset();
    this.spawnProtectionManager.reset();
    this.durabilityManager.reset();
    this.fireballManager.reset();
  }

  public void onCubeJoin() {
    this.serverIP = "play.cubecraft.net";
    this.division = CubeGame.LOBBY;
    this.lastDivision = this.division;
    this.mapName = "Lobby";
    this.teamColour = "";
    this.updateRankString();

    this.eliminated = false;
    this.inPreLobby = true;

    this.gameStartTime = -1;

    this.partyManager.reset();

    LeaderboardAPI.getInstance().loadLeaderboards();
    EggWarsMapAPI.getInstance().loadMaps();
    ChestAPI.getInstance().loadChestLocations();
    ChestAPI.getInstance().loadSeason();
  }

  public void onServerSwitch() {
    this.eliminated = false;
    this.inPreLobby = true;
    this.gameStartTime = -1;
    this.won = false;
    this.spawnProtectionManager.resetHasMap();
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

    GameUpdateEvent e = new GameUpdateEvent(this.division, this.division, this.inPreLobby);
    Laby.fireEvent(e);
  }

  public boolean hasLost() {
    return !won;
  }

  public void setWon(boolean won) {
    this.won = won;
  }

  public boolean hasUpdatedAfterServerSwitch() {
    return hasUpdatedAfterServerSwitch;
  }

  public void setHasUpdatedAfterServerSwitch(boolean hasUpdatedAfterServerSwitch) {
    this.hasUpdatedAfterServerSwitch = hasUpdatedAfterServerSwitch;
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

    GameUpdateEvent e = new GameUpdateEvent(this.lastDivision, this.division, this.inPreLobby);
    Laby.fireEvent(e);
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
    this.mapName = mapName;
    this.currentEggWarsMap = EggWarsMapAPI.getInstance().getEggWarsMapFromCache(mapName);
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

  public String getBungeecord() {
    return bungeecord;
  }

  public void setBungeecord(String bungeecord) {
    this.bungeecord = bungeecord;
  }

  public String getServerID() {
    return serverID;
  }

  public void setServerID(String serverID) {
    this.serverID = serverID;
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

  public @Nullable LoadedEggWarsMap getCurrentEggWarsMap() {
    return currentEggWarsMap;
  }

  public FireballManager getFireballManager() {
    return fireballManager;
  }

}
