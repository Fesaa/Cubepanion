package org.ccu.core.config;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.labymod.api.client.chat.ChatExecutor;
import net.labymod.api.client.network.NetworkPlayerInfo;
import net.labymod.api.client.scoreboard.Scoreboard;
import net.labymod.api.client.scoreboard.ScoreboardObjective;
import net.labymod.api.client.scoreboard.ScoreboardScore;
import org.ccu.core.CCU;
import org.ccu.core.config.subconfig.EggWarsMapInfoSubConfig;
import org.ccu.core.gui.imp.SpawnProtectionComponent;
import org.ccu.core.utils.imp.CrossEggWarsMap;
import org.ccu.core.utils.imp.DoubleCrossEggWarsMap;
import org.ccu.core.utils.imp.SquareEggWarsMap;
import org.ccu.core.utils.imp.base.EggWarsMap;
import org.ccu.core.utils.imp.base.GenLayout;
import org.ccu.core.utils.imp.base.GenLayout.Generator;
import org.ccu.core.utils.imp.base.GenLayout.Location;
import org.ccu.core.utils.imp.base.GenLayout.MapGenerator;
import org.jetbrains.annotations.NotNull;

public class CCUManager {

  private final CCU addon;

  private HashMap<UUID, SpawnProtectionComponent> uuidSpawnProtectionComponentHashMap;
  private HashMap<String, EggWarsMap> eggWarsMapLayouts;

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
    this.eggWarsMapLayouts = new HashMap<>();
    this.registerEggWarsMaps();


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

  private void registerEggWarsMaps() {

    GenLayout YukiGenLayout =
        new GenLayout(new MapGenerator(Generator.DIAMOND, Location.MIDDLE, 2, 4),
            new MapGenerator(Generator.GOLD, Location.MIDDLE, 2, 4),
            new MapGenerator(Generator.DIAMOND, Location.BASE, 0, 1),
            new MapGenerator(Generator.GOLD, Location.BASE, 1, 1),
            new MapGenerator(Generator.IRON, Location.BASE, 2, 1),
            new MapGenerator(Generator.IRON, Location.BASE, 1, 1)
        );
    this.eggWarsMapLayouts.put("Yuki", new CrossEggWarsMap("Yuki", 4, 63, YukiGenLayout,  "yellow", "dark_blue", "green", "red"));

    GenLayout OlympusGenLayout =
        new GenLayout(new MapGenerator(Generator.DIAMOND, Location.MIDDLE, 2, 3),
            new MapGenerator(Generator.GOLD, Location.MIDDLE, 2, 3),
            new MapGenerator(Generator.IRON, Location.MIDDLE, 3, 4),
            new MapGenerator(Generator.DIAMOND, Location.SEMI_MIDDLE, 1, 4),
            new MapGenerator(Generator.GOLD, Location.SEMI_MIDDLE, 2, 4),
            new MapGenerator(Generator.IRON, Location.SEMI_MIDDLE, 3, 8),
            new MapGenerator(Generator.DIAMOND, Location.BASE, 0, 1),
            new MapGenerator(Generator.GOLD, Location.BASE, 1, 2),
            new MapGenerator(Generator.IRON, Location.BASE, 2, 2)
        );
    this.eggWarsMapLayouts.put("Olympus", new CrossEggWarsMap("Olympus", 4, 71, OlympusGenLayout, "green", "dark_aqua", "yellow", "red"));

    GenLayout PalaceGenLayout =
        new GenLayout(new MapGenerator(Generator.DIAMOND, Location.MIDDLE, 3, 2),
            new MapGenerator(Generator.GOLD, Location.MIDDLE, 3, 1),
            new MapGenerator(Generator.IRON, Location.MIDDLE, 3, 1),
            new MapGenerator(Generator.DIAMOND, Location.SEMI_MIDDLE, 2, 4),
            new MapGenerator(Generator.GOLD, Location.SEMI_MIDDLE, 2, 8),
            new MapGenerator(Generator.IRON, Location.SEMI_MIDDLE, 2, 8),
            new MapGenerator(Generator.DIAMOND, Location.BASE, 0, 1),
            new MapGenerator(Generator.GOLD, Location.BASE, 2, 1),
            new MapGenerator(Generator.IRON, Location.BASE, 2, 2),
            new MapGenerator(Generator.IRON, Location.BASE, 1, 1)
        );
    this.eggWarsMapLayouts.put("Palace", new CrossEggWarsMap("Palace", 4, 70, PalaceGenLayout, "dark_purple", "yellow", "dark_blue", "red"));

    GenLayout RuinsGenLayout =
        new GenLayout(new MapGenerator(Generator.DIAMOND, Location.MIDDLE, 2, 2),
            new MapGenerator(Generator.DIAMOND, Location.SEMI_MIDDLE, 2, 2),
            new MapGenerator(Generator.GOLD, Location.SEMI_MIDDLE, 2, 2),
            new MapGenerator(Generator.IRON, Location.SEMI_MIDDLE, 3, 2),
            new MapGenerator(Generator.DIAMOND, Location.BASE, 0, 2),
            new MapGenerator(Generator.GOLD, Location.BASE, 2, 1),
            new MapGenerator(Generator.GOLD, Location.BASE, 1, 1),
            new MapGenerator(Generator.IRON, Location.BASE, 2, 2),
            new MapGenerator(Generator.IRON, Location.BASE, 1, 1)
        );
    this.eggWarsMapLayouts.put("Ruins", new CrossEggWarsMap("Ruins", 4, 86, RuinsGenLayout, "dark_purple", "red", "green", "dark_blue"));

    GenLayout BeachGenLayout =
        new GenLayout(new MapGenerator(Generator.DIAMOND, Location.MIDDLE, 2, 2),
            new MapGenerator(Generator.GOLD, Location.MIDDLE, 4, 2),
            new MapGenerator(Generator.DIAMOND, Location.BASE, 0, 1),
            new MapGenerator(Generator.GOLD, Location.BASE, 1, 1),
            new MapGenerator(Generator.IRON, Location.BASE, 1, 2),
            new MapGenerator(Generator.IRON, Location.BASE, 0, 1)
        );
    this.eggWarsMapLayouts.put("Beach", new CrossEggWarsMap("Beach", 4, 86, BeachGenLayout, "dark_purple", "green", "gold", "aqua"));

    GenLayout FairytaleGenLayout =
        new GenLayout(new MapGenerator(Generator.DIAMOND, Location.MIDDLE, 2, 2),
            new MapGenerator(Generator.GOLD, Location.MIDDLE, 3, 2),
            new MapGenerator(Generator.IRON, Location.MIDDLE, 3, 4),
            new MapGenerator(Generator.DIAMOND, Location.BASE, 0, 1),
            new MapGenerator(Generator.GOLD, Location.BASE, 3, 1),
            new MapGenerator(Generator.IRON, Location.BASE, 2, 1),
            new MapGenerator(Generator.IRON, Location.BASE, 1, 1)
        );
    this.eggWarsMapLayouts.put("Fairytale", new CrossEggWarsMap("Fairytale", 4, 64, FairytaleGenLayout, "dark_blue", "red", "dark_purple", "yellow"));


    GenLayout CyberSnowGenLayout =
        new GenLayout(new MapGenerator(Generator.DIAMOND, Location.SEMI_MIDDLE, 1, 8),
            new MapGenerator(Generator.GOLD, Location.SEMI_MIDDLE, 2, 8),
            new MapGenerator(Generator.DIAMOND, Location.BASE, 0, 1),
            new MapGenerator(Generator.GOLD, Location.BASE, 1, 1),
            new MapGenerator(Generator.IRON, Location.BASE, 2, 1),
            new MapGenerator(Generator.IRON, Location.BASE, 1, 1)
        );
    this.eggWarsMapLayouts.put("Cyber Snow", new DoubleCrossEggWarsMap("Cyber Snow", 2, 77, CyberSnowGenLayout,
        List.of("dark_blue", "red"), List.of("gray", "light_purple"), List.of("dark_gray", "dark_aqua"), List.of("yellow", "green")));


    GenLayout MushroomGenLayout =
        new GenLayout(new MapGenerator(Generator.DIAMOND, Location.MIDDLE, 1, 2),
            new MapGenerator(Generator.GOLD, Location.MIDDLE, 3, 2),
            new MapGenerator(Generator.DIAMOND, Location.SEMI_MIDDLE, 1, 2),
            new MapGenerator(Generator.IRON, Location.SEMI_MIDDLE, 3, 2),
            new MapGenerator(Generator.DIAMOND, Location.BASE, 0, 1),
            new MapGenerator(Generator.GOLD, Location.BASE, 3, 1),
            new MapGenerator(Generator.IRON, Location.BASE, 2, 1),
            new MapGenerator(Generator.IRON, Location.BASE, 1, 1)
        );
    this.eggWarsMapLayouts.put("Mushroom", new SquareEggWarsMap("Mushroom", 4, 72, MushroomGenLayout, List.of("green", "red"), List.of("yellow", "aqua")));

    GenLayout ModernGenLayout =
        new GenLayout(new MapGenerator(Generator.DIAMOND, Location.MIDDLE, 2, 2),
            new MapGenerator(Generator.GOLD, Location.MIDDLE, 2, 2),
            new MapGenerator(Generator.IRON, Location.MIDDLE, 3, 2),
            new MapGenerator(Generator.DIAMOND, Location.BASE, 0, 1),
            new MapGenerator(Generator.GOLD, Location.BASE, 1, 1),
            new MapGenerator(Generator.IRON, Location.BASE, 1, 1),
            new MapGenerator(Generator.IRON, Location.BASE, 0, 1)
        );
    this.eggWarsMapLayouts.put("Modern", new SquareEggWarsMap("Modern", 2, 54, ModernGenLayout, List.of("yellow", "red"), List.of("aqua", "light_purple")));

    GenLayout CyberCityGenLayout =
        new GenLayout(new MapGenerator(Generator.DIAMOND, Location.MIDDLE, 1, 3),
            new MapGenerator(Generator.GOLD, Location.MIDDLE, 2, 6),
            new MapGenerator(Generator.IRON, Location.MIDDLE, 2, 6),
            new MapGenerator(Generator.DIAMOND, Location.BASE, 0, 1),
            new MapGenerator(Generator.GOLD, Location.BASE, 1, 1),
            new MapGenerator(Generator.IRON, Location.BASE, 2, 1),
            new MapGenerator(Generator.IRON, Location.BASE, 1, 1)
        );
    this.eggWarsMapLayouts.put("Cyber City", new SquareEggWarsMap("Cyber City", 2, 67, CyberCityGenLayout, List.of("light_purple", "dark_aqua"), List.of("red", "green")));

  }

  public boolean doEggWarsMapLayout(String mapName, boolean keyBind) {
    ChatExecutor chat = this.addon.labyAPI().minecraft().chatExecutor();
    EggWarsMap map = this.eggWarsMapLayouts.get(mapName);
    EggWarsMapInfoSubConfig config = this.addon.configuration().getEggWarsMapInfoSubConfig();
    if (map == null) {
      return false;
    }
    chat.displayClientMessage(Component.text("Map Info for " + mapName, NamedTextColor.GOLD));
    map.setCurrentTeamColour(this.teamColour);
    Component mapLayout = map.getMapLayoutComponent();
    if (mapLayout != null) {
      chat.displayClientMessage(mapLayout);
    }
    Component buildLimit = map.getBuildLimitMessage();
    if (buildLimit != null) {
      chat.displayClientMessage(buildLimit);
    }
    if (config.getGenLayout().get() && !keyBind) {
      chat.displayClientMessage(map.getGenLayoutComponent());
    }
    return true;
  }

  public void doEggWarsMapLayout() {
    EggWarsMapInfoSubConfig subConfig = this.addon.configuration().getEggWarsMapInfoSubConfig();
    if (!subConfig.isEnabled().get()) {
      return;
    }
    this.setCurrentTeamColour();

    ChatExecutor chat = this.addon.labyAPI().minecraft().chatExecutor();

    if (subConfig.getMapLayout().get()) {
      Component mapLayout = this.getMapLayoutComponent();
      if (mapLayout != null) {
        chat.displayClientMessage(mapLayout);
      }
    }

    if (subConfig.getBuildLimit().get()) {
      Component buildLimit = this.getBuildLimitMessage();
      if (buildLimit != null) {
        chat.displayClientMessage(buildLimit);
      }
    }

    Component genLayoutComponent = this.getGenLayoutComponent();
    if (genLayoutComponent != null) {
      chat.displayClientMessage(genLayoutComponent);
    }

    if (subConfig.getLogInParty().get()) {
      if (this.isInParty()) {
        String partyMessage = this.getPartyMessage();
        if (partyMessage != null) {
          chat.chat(partyMessage, false);
        }
      }
    }
  }

  private Component getGenLayoutComponent() {
    EggWarsMap map = this.eggWarsMapLayouts.get(this.mapName);
    if (map == null) {
      return null;
    }
    return map.getGenLayoutComponent();
  }

  private void setCurrentTeamColour() {
    EggWarsMap map = this.eggWarsMapLayouts.get(this.mapName);
    if (map != null) {
      map.setCurrentTeamColour(this.teamColour);
    }
  }

  private Component getMapLayoutComponent() {
    EggWarsMap map = this.eggWarsMapLayouts.get(this.mapName);
    if (map == null) {
      return null;
    }
    return map.getMapLayoutComponent();
  }

  private Component getBuildLimitMessage() {
    EggWarsMap map = this.eggWarsMapLayouts.get(this.mapName);
    if (map == null) {
      return null;
    }
    return map.getBuildLimitMessage();
  }

  private String getPartyMessage() {
    EggWarsMap map = this.eggWarsMapLayouts.get(this.mapName);
    if (map == null) {
      return null;
    }
    return map.getPartyMessage();
  }

  public String debugVars() {
    return "ServerIp: " + this.serverIP
        + "\nDivisionName: " + this.divisionName
        + "\nLastDivisionName: " + this.lastDivisionName
        + "\nMapName: " + this.mapName
        + "\nEliminated: " + this.eliminated
        + "\nInPreLobby: " + this.inPreLobby
        + "\nInParty: " + this.inParty
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
    }
    for (Component component : playerInfo.getTeam().formatDisplayName(playerInfo.displayName()).children()) {
      if (!((TextComponent) component).content().equals("")) {
        teamColour = Objects.requireNonNull(component.color()).toString();
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
