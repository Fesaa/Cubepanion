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