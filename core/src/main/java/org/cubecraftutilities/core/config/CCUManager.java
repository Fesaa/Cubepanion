package org.cubecraftutilities.core.config;

import java.util.Objects;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.network.NetworkPlayerInfo;
import net.labymod.api.client.scoreboard.Scoreboard;
import net.labymod.api.client.scoreboard.ScoreboardObjective;
import net.labymod.api.client.scoreboard.ScoreboardScore;
import org.cubecraftutilities.core.CCU;
import org.cubecraftutilities.core.config.submanagers.DurabilityManager;
import org.cubecraftutilities.core.config.submanagers.EggWarsMapInfoManager;
import org.cubecraftutilities.core.config.submanagers.FriendTrackerManager;
import org.cubecraftutilities.core.config.submanagers.PartyManager;
import org.cubecraftutilities.core.config.submanagers.SpawnProtectionManager;
import org.jetbrains.annotations.NotNull;

public class CCUManager {

  private final CCU addon;

  // Sub Managers

  private final PartyManager partyManager;
  private final EggWarsMapInfoManager eggWarsMapInfoManager;
  private final SpawnProtectionManager spawnProtectionManager;
  private final DurabilityManager durabilityManager;
  private final FriendTrackerManager friendTrackerManager;

  // Own fields

  private String serverIP;
  private String divisionName;
  private String lastDivisionName;
  private String mapName;
  private String teamColour;
  private String bungeecord;
  private String serverID;

  private boolean eliminated;
  private boolean inPreLobby;
  private boolean won;

  private boolean requestedFullFriendsList;

  private int chestPartyAnnounceCounter;

  // TEMP
  private boolean changedColour = false;



  public CCUManager(CCU addon) {
    this.addon = addon;

    this.partyManager = new PartyManager();
    this.eggWarsMapInfoManager = new EggWarsMapInfoManager(addon);
    this.spawnProtectionManager = new SpawnProtectionManager(addon);
    this.durabilityManager = new DurabilityManager();
    this.friendTrackerManager = new FriendTrackerManager();

    this.serverIP = "";
    this.divisionName = "";
    this.lastDivisionName = "";
    this.mapName = "";
    this.teamColour = "";
    this.bungeecord = "";
    this.serverID = "";

    this.eliminated = false;
    this.inPreLobby = false;
    this.won = false;
    this.requestedFullFriendsList = false;

    this.chestPartyAnnounceCounter = 0;
  }



  public PartyManager getPartyManager() {return this.partyManager;}
  public EggWarsMapInfoManager getEggWarsMapInfoManager() {return this.eggWarsMapInfoManager;}
  public DurabilityManager getDurabilityManager() {return this.durabilityManager;}
  public SpawnProtectionManager getSpawnProtectionManager() {return spawnProtectionManager;}
  public FriendTrackerManager getFriendTrackerManager() {return friendTrackerManager;}

  public void reset() {
    this.serverIP = "";
    this.lastDivisionName = "";
    this.divisionName = "";
    this.teamColour = "";
    this.mapName = "";
    this.bungeecord = "";
    this.serverID = "";

    this.eliminated = false;
    this.inPreLobby = false;
    this.won = false;
    this.requestedFullFriendsList = false;

    this.chestPartyAnnounceCounter = 0;

    this.partyManager.reset();
    this.durabilityManager.reset();
    this.friendTrackerManager.endCurrentLoop();
    this.friendTrackerManager.resetTrackers();
  }

  public void onCubeJoin() {
    this.serverIP = "play.cubecraft.net";
    this.divisionName = "CubeCraft";
    this.lastDivisionName = this.divisionName;
    this.mapName = "Lobby";
    this.teamColour = "";

    this.eliminated = false;
    this.inPreLobby = true;
    this.changedColour = false;

    this.chestPartyAnnounceCounter = 0;

    this.partyManager.reset();
  }

  public void onServerSwitch() {
    this.eliminated = false;
    this.inPreLobby = true;
    this.changedColour = false;
  }

  public void registerNewDivision(@NotNull Scoreboard scoreboard, @NotNull ScoreboardObjective scoreboardObjective) {
    this.spawnProtectionManager.resetHasMap();

    this.lastDivisionName = this.divisionName;

    Component title = scoreboardObjective.getTitle();

    if (title.getChildren().size() == 0) { // Prod
      this.divisionName = ((TextComponent) title).getText();
    } else { // dev2
      this.divisionName = ((TextComponent) title.getChildren().get(0)).getText();
    }
    this.mapName = getMap(scoreboard, scoreboardObjective);

    this.eliminated = false;
    this.won = false;
  }

  public void updateTeamColour() {
    if (this.changedColour) {
      return;
    }
    ClientPlayer clientPlayer = this.addon.labyAPI().minecraft().getClientPlayer();
    if (clientPlayer == null) {
      return;
    }
    NetworkPlayerInfo playerInfo = clientPlayer.networkPlayerInfo();
    if (playerInfo == null) {
      return;
    }
    for (Component component : playerInfo.getTeam().formatDisplayName(playerInfo.displayName()).getChildren()) {
      if (!((TextComponent) component).getText().equals("")) {
        teamColour = Objects.requireNonNull(component.getColor()).toString();
        return;
      }
    }
  }

  public void setTeamColour(String teamColour) {
    this.teamColour = teamColour;
    this.changedColour = true;
  }

  private String getMap(@NotNull Scoreboard scoreboard, ScoreboardObjective scoreboardObjective) {
    ScoreboardScore lastEntry = null;
    for (ScoreboardScore scoreboardScore : scoreboard.getScores(scoreboardObjective)) {
      if (scoreboardScore.getName().contains("Map:")) {
        if (lastEntry != null) {
          return lastEntry.getName().substring(2);
        }
      }
      lastEntry = scoreboardScore;
    }
    return "";
  }

  public void setChangedColour(boolean changedColour) {this.changedColour = changedColour;}

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

  public boolean isWon() {
    return won;
  }

  public void setWon(boolean won) {
    this.won = won;
  }

  public int getChestPartyAnnounceCounter() {
    return chestPartyAnnounceCounter;
  }

  public String getDivisionName() {
    return divisionName;
  }

  public String getLastDivisionName() {
    return lastDivisionName;
  }

  public String getMapName() {
    return mapName;
  }

  public String getServerIP() {
    return serverIP;
  }

  public String getTeamColour() {
    return teamColour;
  }

  public String getBungeecord() {
    return bungeecord;
  }

  public String getServerID() {
    return serverID;
  }

  public void setBungeecord(String bungeecord) {
    this.bungeecord = bungeecord;
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
}
