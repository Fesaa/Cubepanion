package org.ccu.core.config;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.labymod.api.client.network.NetworkPlayerInfo;
import net.labymod.api.client.scoreboard.Scoreboard;
import net.labymod.api.client.scoreboard.ScoreboardObjective;
import net.labymod.api.client.scoreboard.ScoreboardScore;
import org.ccu.core.CCU;
import org.ccu.core.gui.imp.SpawnProtectionComponent;
import org.jetbrains.annotations.NotNull;

public class CCUManager {

  private final CCU addon;

  private HashMap<UUID, SpawnProtectionComponent> uuidSpawnProtectionComponentHashMap;
  private String serverIP;
  private String divisionName;
  private String lastDivisionName;
  private String mapName;
  private String teamColour;

  private boolean eliminated;
  private boolean inPreLobby;
  private boolean inParty;
  private boolean won;

  private int chestPartyAnnounceCounter;
  private int totalHelmetDurability = 0;
  private int totalChestPlateDurability = 0;
  private int totalLeggingsDurability = 0;
  private int totalBootsDurability = 0;



  public CCUManager(CCU addon) {
    this.addon = addon;
    this.uuidSpawnProtectionComponentHashMap = new HashMap<>();
    this.serverIP = "";
    this.divisionName = "";
    this.lastDivisionName = "";
    this.mapName = "";
    this.teamColour = "";

    this.eliminated = false;
    this.inParty = false;
    this.inPreLobby = false;
    this.won = false;

    this.chestPartyAnnounceCounter = 0;
  }

  public void reset() {
    this.serverIP = "";
    this.lastDivisionName = "";
    this.divisionName = "";
    this.teamColour = "";
    this.mapName = "";

    this.eliminated = false;
    this.inParty = false;
    this.inPreLobby = false;

    this.chestPartyAnnounceCounter = 0;
  }

  public void onCubeJoin() {
    this.serverIP = "play.cubecraft.net";
    this.divisionName = "CubeCraft";
    this.lastDivisionName = this.divisionName;
    this.mapName = "Lobby";
    this.teamColour = "";

    this.eliminated = false;
    this.inParty = false;
    this.inPreLobby = true;

    this.chestPartyAnnounceCounter = 0;
  }

  public void registerNewDivision(@NotNull Scoreboard scoreboard, @NotNull ScoreboardObjective scoreboardObjective) {
    this.uuidSpawnProtectionComponentHashMap = new HashMap<>();

    this.lastDivisionName = this.divisionName;
    this.divisionName = ((TextComponent) scoreboardObjective.getTitle()).content();
    this.mapName = getMap(scoreboard, scoreboardObjective);

    this.eliminated = false;
    this.won = false;
  }

  public void updateTeamColour() {
    NetworkPlayerInfo playerInfo = this.addon.labyAPI().minecraft().clientPlayer().networkPlayerInfo();
    if (playerInfo == null) {
      return;
    };
    for (Component component : playerInfo.getTeam().formatDisplayName(playerInfo.displayName()).children()) {
      if (!((TextComponent) component).content().equals("")) {
        teamColour = Objects.requireNonNull(((TextComponent) component).color()).toString();
        return;
      }
    }
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

  public void registerDeath(UUID uuid) {
    SpawnProtectionComponent spawnProtectionComponent = new SpawnProtectionComponent(this.addon);
    spawnProtectionComponent.enable();
    this.uuidSpawnProtectionComponentHashMap.put(uuid, spawnProtectionComponent);
  }

  public SpawnProtectionComponent getSpawnProtectionComponent(UUID uuid) {
    return this.uuidSpawnProtectionComponentHashMap.get(uuid);
  }

  public HashMap<UUID, SpawnProtectionComponent> getUuidSpawnProtectionComponentHashMap() {
    return uuidSpawnProtectionComponentHashMap;
  }

  public void updateSpawnProtectionComponentHashMap(boolean endOfSecond) {
    for (SpawnProtectionComponent spawnProtectionComponentGen : this.uuidSpawnProtectionComponentHashMap.values()) {
      if (spawnProtectionComponentGen != null) {
        spawnProtectionComponentGen.update(endOfSecond);
      }
    }
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

  public boolean isInParty() {
    return inParty;
  }

  public void setInParty(boolean inParty) {
    this.inParty = inParty;
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

  public int getTotalBootsDurability() {
    return totalBootsDurability;
  }

  public void setTotalBootsDurability(int totalBootsDurability) {
    this.totalBootsDurability = totalBootsDurability;
  }

  public int getTotalChestPlateDurability() {
    return totalChestPlateDurability;
  }

  public void setTotalChestPlateDurability(int totalChestPlateDurability) {
    this.totalChestPlateDurability = totalChestPlateDurability;
  }

  public int getTotalHelmetDurability() {
    return totalHelmetDurability;
  }

  public void setTotalHelmetDurability(int totalHelmetDurability) {
    this.totalHelmetDurability = totalHelmetDurability;
  }

  public int getTotalLeggingsDurability() {
    return totalLeggingsDurability;
  }

  public void setTotalLeggingsDurability(int totalLeggingsDurability) {
    this.totalLeggingsDurability = totalLeggingsDurability;
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
