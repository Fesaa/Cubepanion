package org.cubepanion.core.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import net.labymod.api.Laby;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.managers.submanagers.DurabilityManager;
import org.cubepanion.core.managers.submanagers.EggWarsMapInfoManager;
import org.cubepanion.core.managers.submanagers.FriendTrackerManager;
import org.cubepanion.core.managers.submanagers.PartyManager;
import org.cubepanion.core.managers.submanagers.SpawnProtectionManager;
import org.cubepanion.core.utils.CubeGame;
import org.cubepanion.core.utils.LOGGER;
import org.cubepanion.core.utils.eggwarsmaps.base.EggWarsMap;
import org.cubepanion.core.weave.ChestAPI;
import org.cubepanion.core.weave.ChestAPI.ChestLocation;
import org.cubepanion.core.weave.ChestAPI.SeasonType;
import org.cubepanion.core.weave.LeaderboardAPI;
import org.jetbrains.annotations.Nullable;

public class CubepanionManager {
  // Sub Managers

  private final PartyManager partyManager;
  private final EggWarsMapInfoManager eggWarsMapInfoManager;
  private final SpawnProtectionManager spawnProtectionManager;
  private final DurabilityManager durabilityManager;
  private final FriendTrackerManager friendTrackerManager;

  // Own fields

  private String serverIP;
  private CubeGame division;
  private CubeGame lastDivision;
  private String mapName;
  private String teamColour;
  private String bungeecord;
  private String serverID;
  private String rankString;
  private EggWarsMap currentEggWarsMap;

  private boolean eliminated;
  private boolean inPreLobby;
  private boolean won;
  private boolean hasUpdatedAfterServerSwitch;

  private boolean requestedFullFriendsList;
  private boolean requestedRankString;

  private int chestPartyAnnounceCounter;

  private long gameStartTime;


  public CubepanionManager(Cubepanion addon) {
    this.partyManager = new PartyManager();
    this.eggWarsMapInfoManager = new EggWarsMapInfoManager(addon);
    this.spawnProtectionManager = new SpawnProtectionManager(addon);
    this.durabilityManager = new DurabilityManager();
    this.friendTrackerManager = new FriendTrackerManager();

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
    this.requestedFullFriendsList = false;
    this.hasUpdatedAfterServerSwitch = false;

    this.chestPartyAnnounceCounter = 0;
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

  public FriendTrackerManager getFriendTrackerManager() {
    return friendTrackerManager;
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
    this.requestedFullFriendsList = false;
    this.hasUpdatedAfterServerSwitch = false;

    this.chestPartyAnnounceCounter = 0;
    this.gameStartTime = -1;

    this.partyManager.reset();
    this.durabilityManager.reset();
    this.friendTrackerManager.endCurrentLoop();
    this.friendTrackerManager.resetTrackers();

    ChestAPI.getInstance().clearChestLocations();
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

    this.chestPartyAnnounceCounter = 0;
    this.gameStartTime = -1;

    this.partyManager.reset();
    this.eggWarsMapInfoManager.queryMaps();

    LeaderboardAPI.getInstance().loadLeaderboards();

    try {
      ChestLocation[] chestLocations = ChestAPI.getInstance()
          .getCurrentChestLocations()
          .exceptionally(throwable -> {
            LOGGER.error(getClass(), throwable, "Could not update Cubepanion#chestLocations");
            return new ChestLocation[0];
          })
          .get(500, TimeUnit.MILLISECONDS);
       ChestAPI.getInstance().setChestLocations(List.of(chestLocations));
    } catch (InterruptedException | TimeoutException e) {
      LOGGER.error(getClass(), e, "ChestAPI#getCurrentChestLocations took longer than 500ms");
    } catch (ExecutionException e) {
      LOGGER.error(getClass(), e.getCause(), "ChestAPI#getCurrentChestLocations completed exceptionally");
    }
    try {
      String[] seasons = ChestAPI.getInstance()
          .getSeasons(SeasonType.RUNNING)
          .exceptionally(throwable -> {
            LOGGER.error(getClass(), throwable, "Could not update Cubepanion#season");
            return new String[0];
          })
          .get(500, TimeUnit.MILLISECONDS);
      if (seasons.length > 0) {
        ChestAPI.getInstance().setSeason(seasons[0]);
      }
    } catch (InterruptedException | TimeoutException e) {
      LOGGER.error(getClass(), e, "ChestAPI#getSeasons took longer than 500ms");
    } catch (ExecutionException e) {
      LOGGER.error(getClass(), e.getCause(), "ChestAPI#getSeasons completed exceptionally");
    }
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

  public int getChestPartyAnnounceCounter() {
    return chestPartyAnnounceCounter;
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
    this.mapName = mapName;
    this.currentEggWarsMap = eggWarsMapInfoManager.getEggWarsMap(mapName);
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

  public boolean hasRequestedFullFriendsList() {
    return requestedFullFriendsList;
  }

  public void setRequestedFullFriendsList(boolean requestedFullFriendsList) {
    this.requestedFullFriendsList = requestedFullFriendsList;
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
    this.requestedRankString = true;
    Laby.labyAPI().minecraft().chatExecutor().chat("/who", false);
  }

  public boolean hasRequestedRankString() {
    return requestedRankString;
  }

  public void setRequestedRankString(boolean requestedRankString) {
    this.requestedRankString = requestedRankString;
  }

  public @Nullable EggWarsMap getCurrentEggWarsMap() {
    return currentEggWarsMap;
  }

}
