package org.ccu.core.config.internal;

import com.google.gson.JsonObject;
import org.ccu.core.CCU;

import java.io.File;
import java.util.ArrayList;

public class ConfigManager {

  private final CCU addon;

  public ConfigObjectClass staticLobbyChestLocations;
  public ConfigObjectClass mapInfo;
  public ConfigObjectClass mainConfig;

  private ArrayList<ConfigObjectClass> configObjectClasses;

  public ConfigManager(CCU addon) {
    this.addon = addon;
  }

  public void initConfig() {

    configObjectClasses = new ArrayList<>();

    File configDir = new File("./config/LabyModCCU");
    if (configDir.mkdirs()) {
      this.addon.logger().warn("Config directory was not present; I made it. Bug? Or first time using the CCU addon?");
    }

    // Chest Locations
    JsonObject chestLocations = new JsonObject();
    chestLocations.addProperty("current-event", "");
    staticLobbyChestLocations = (new ConfigObjectClass()).make(this.addon, new File("./config/LabyModCCU/LobbyChestLocations.json"), chestLocations);
    configObjectClasses.add(staticLobbyChestLocations);

    // EggWars Map Info
    JsonObject mapInfoJson = new JsonObject();
    mapInfoJson.add("teamColourOrder", new JsonObject());
    mapInfoJson.add("teamBuildLimit", new JsonObject());

    mapInfo = (new ConfigObjectClass()).make(this.addon, new File("./config/LabyModCCU/EggWarsMapInfo.json"), mapInfoJson);
    configObjectClasses.add(mapInfo);

    // Config
    JsonObject needyFesaConfigJson = new JsonObject();
    needyFesaConfigJson.addProperty("autoVote", true);
    needyFesaConfigJson.addProperty("minWaitTime", 50);
    needyFesaConfigJson.addProperty("maxWaitTime", 1000);
    needyFesaConfigJson.addProperty("development-mode", false);
    needyFesaConfigJson.addProperty("chestFinder", true);

    JsonObject eggWarsVoting = new JsonObject();
    eggWarsVoting.addProperty("leftVoteId", 16);
    eggWarsVoting.addProperty("middleVoteId", -1);
    eggWarsVoting.addProperty("rightVoteId", 10);
    eggWarsVoting.addProperty("leftChoiceId", 11);
    eggWarsVoting.addProperty("middleChoiceId", -1);
    eggWarsVoting.addProperty("rightChoiceId", 15);
    eggWarsVoting.addProperty("hotBarSlot", 2);

    JsonObject soloSkyWarsVoting = new JsonObject();
    soloSkyWarsVoting.addProperty("leftVoteId", 16);
    soloSkyWarsVoting.addProperty("middleVoteId", 13);
    soloSkyWarsVoting.addProperty("rightVoteId", 10);
    soloSkyWarsVoting.addProperty("leftChoiceId", 10);
    soloSkyWarsVoting.addProperty("middleChoiceId", 13);
    soloSkyWarsVoting.addProperty("rightChoiceId", 16);
    soloSkyWarsVoting.addProperty("hotBarSlot", 1);

    JsonObject luckyIslandsVoting = new JsonObject();
    luckyIslandsVoting.addProperty("leftVoteId", 14);
    luckyIslandsVoting.addProperty("middleVoteId", -1);
    luckyIslandsVoting.addProperty("rightVoteId", 10);
    luckyIslandsVoting.addProperty("leftChoiceId", 11);
    luckyIslandsVoting.addProperty("middleChoiceId", -1);
    luckyIslandsVoting.addProperty("rightChoiceId", 15);
    luckyIslandsVoting.addProperty("hotBarSlot", 1);

    needyFesaConfigJson.add("Team EggWars", eggWarsVoting);
    needyFesaConfigJson.add("Solo SkyWars", soloSkyWarsVoting);
    needyFesaConfigJson.add("Lucky Islands", luckyIslandsVoting);

    mainConfig = (new ConfigObjectClass()).make(this.addon, new File("./config/LabyModCCU/CCUmain.json"), needyFesaConfigJson);
    configObjectClasses.add(mainConfig);
  }

  public void loadConfig() {
    for (ConfigObjectClass configObjectClass: configObjectClasses) {
      configObjectClass.configure();
    }
  }

  public void saveConfig() {
    for (ConfigObjectClass configObjectClass : configObjectClasses) {
      configObjectClass.save();
    }
  }
}