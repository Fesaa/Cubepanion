package org.ccu.core.config;

import java.util.Objects;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.labymod.api.client.network.NetworkPlayerInfo;
import net.labymod.api.client.scoreboard.Scoreboard;
import net.labymod.api.client.scoreboard.ScoreboardObjective;
import net.labymod.api.client.scoreboard.ScoreboardScore;
import org.ccu.core.CCU;
import org.ccu.core.config.submanagers.DurabilityManager;
import org.ccu.core.config.submanagers.EggWarsMapInfoManager;
import org.ccu.core.config.submanagers.PartyManager;
import org.ccu.core.config.submanagers.SpawnProtectionManager;
import org.jetbrains.annotations.NotNull;

public class CCUManager {

  private final CCU addon;

  // Sub Managers

  private final PartyManager partyManager;
  private final EggWarsMapInfoManager eggWarsMapInfoManager;
  private final SpawnProtectionManager spawnProtectionManager;
  private final DurabilityManager durabilityManager;

  // Own fields

  private String serverIP;
  private String divisionName;
  private String lastDivisionName;
  private String mapName;
  private String teamColour;

  private boolean eliminated;
  private boolean inPreLobby;
  private boolean won;

  private int chestPartyAnnounceCounter;

  // TEMP
  private boolean changedColour = false;



  public CCUManager(CCU addon) {
    this.addon = addon;

    this.partyManager = new PartyManager();
    this.eggWarsMapInfoManager = new EggWarsMapInfoManager(addon);
    this.spawnProtectionManager = new SpawnProtectionManager(addon);
    this.durabilityManager = new DurabilityManager();

    this.serverIP = "";
    this.divisionName = "";
    this.lastDivisionName = "";
    this.mapName = "";
    this.teamColour = "";

    this.eliminated = false;
    this.inPreLobby = false;
    this.won = false;

    this.chestPartyAnnounceCounter = 0;
  }



  public PartyManager getPartyManager() {
    return this.partyManager;
  }
  public EggWarsMapInfoManager getEggWarsMapInfoManager() {
    return this.eggWarsMapInfoManager;
  }
  public DurabilityManager getDurabilityManager() {return this.durabilityManager;}
  public SpawnProtectionManager getSpawnProtectionManager() {return spawnProtectionManager;}

  public String debugVars() {
    return "ServerIp: " + this.serverIP
        + "\nDivisionName: " + this.divisionName
        + "\nLastDivisionName: " + this.lastDivisionName
        + "\nMapName: " + this.mapName
        + "\nEliminated: " + this.eliminated
        + "\nInPreLobby: " + this.inPreLobby
        + "\nInParty: " + this.partyManager.isInParty()
        + "\nWon: " + this.won
        + "\nChestPartyAnnounceCounter: " + this.chestPartyAnnounceCounter;
  }

  public void reset() {
    this.serverIP = "";
    this.lastDivisionName = "";
    this.divisionName = "";
    this.teamColour = "";
    this.mapName = "";

    this.eliminated = false;
    this.inPreLobby = false;

    this.chestPartyAnnounceCounter = 0;

    this.partyManager.reset();
    this.durabilityManager.reset();
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

    if (title.children().size() == 0) { // Prod
      this.divisionName = ((TextComponent) title).content();
    } else { // dev2
      this.divisionName = ((TextComponent) title.children().get(0)).content();
    }
    this.mapName = getMap(scoreboard, scoreboardObjective);

    this.eliminated = false;
    this.won = false;
  }

  public void updateTeamColour() {
    if (this.changedColour) {
      return;
    }
    NetworkPlayerInfo playerInfo = this.addon.labyAPI().minecraft().clientPlayer().networkPlayerInfo();
    if (playerInfo == null) {
      return;
    }
    for (Component component : playerInfo.getTeam().formatDisplayName(playerInfo.displayName()).children()) {
      if (!((TextComponent) component).content().equals("")) {
        teamColour = Objects.requireNonNull(component.color()).toString();
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
}
