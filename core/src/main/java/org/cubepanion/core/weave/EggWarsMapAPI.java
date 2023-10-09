package org.cubepanion.core.weave;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.gfx.pipeline.post.data.EffectData;
import org.cubepanion.core.utils.LOGGER;
import org.cubepanion.core.utils.eggwarsmaps.base.LoadedEggWarsMap;
import javax.inject.Singleton;

import static org.cubepanion.core.utils.Utils.fromAPIMap;
import static org.cubepanion.core.weave.Utils.makeRequest;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

@Singleton
public class EggWarsMapAPI {

  private static EggWarsMapAPI instance;

  public static EggWarsMapAPI getInstance() {
    return instance;
  }

  private static final String baseURL = "https://ameliah.art/cubepanion_api";

  private final HashMap<String, LoadedEggWarsMap> convertedEggWarsMaps = new HashMap<>();

  public static void Init() {
    instance = new EggWarsMapAPI();
  }

   private EggWarsMapAPI() {
    if (instance != null) {
      throw new RuntimeException("Class already initialized");
    }
    instance = this;
  }

  public void loadMaps() {
    getAllEggWarsMaps()
        .whenComplete((eggWarsMaps, throwable) -> {
          if (throwable != null) {
            LOGGER.error(getClass(), throwable, "Could not load EggWars maps.");
            return;
          }

          for (EggWarsMapAPI.EggWarsMap map : eggWarsMaps) {
            LoadedEggWarsMap eggWarsMap = fromAPIMap(map);
            if (eggWarsMap != null) {
              convertedEggWarsMaps.put(map.map_name().toLowerCase(), eggWarsMap);
            } else {
              LOGGER.warn(getClass(), "Could not convert EggWars map: " + map.map_name());
            }
          }
        });
  }

  public LoadedEggWarsMap getEggWarsMapFromCache(String name) {
    return convertedEggWarsMaps.get(name.toLowerCase());
  }


  public Component getAllMapNames() {
    Component out = Component.empty();
    for (String name : convertedEggWarsMaps.keySet()) {
      out = out.append(Component.text(name))
          .append(Component.text(", "));
    }
    return out;
  }

  /**
   * Get all EggWars maps
   *
   * @return all EggWars maps
   */
  public CompletableFuture<EggWarsMap[]> getAllEggWarsMaps() {
    String url = baseURL + "/eggwars_map_api";
    return makeRequest(url, EggWarsMap[].class);
  }

  /**
   * Get EggWars map by name
   *
   * @param name map name
   * @return EggWars map
   */
  public CompletableFuture<EggWarsMap> getEggWarsMap(String name) {
    String url = baseURL + "/eggwars_map_api/" + name;
    return makeRequest(url, EggWarsMap.class);
  }

  /**
   * EggWars map
   */
  public record EggWarsMap(String unique_name, String map_name, String layout, int team_size,
                           int build_limit,
                           String colours, Generator[] generators) {

  }

  /**
   * Generator
   */
  public record Generator(String unique_name, int ordering, String gen_type, String gen_location,
                          int level,
                          int count) {

  }

}
