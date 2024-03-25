package art.ameliah.laby.addons.cubepanion.core.weave;

import static art.ameliah.laby.addons.cubepanion.core.utils.Utils.fromAPIMap;
import static art.ameliah.laby.addons.cubepanion.core.weave.Utils.makeRequest;

import art.ameliah.laby.addons.cubepanion.core.utils.LOGGER;
import art.ameliah.laby.addons.cubepanion.core.utils.eggwarsmaps.base.LoadedEggWarsMap;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import javax.inject.Singleton;
import net.labymod.api.client.component.Component;

@Singleton
public class EggWarsMapAPI {

  private static final String baseURL = System.getenv("CUBEPANION_DEV") != null
      ? "http://127.0.0.1/cubepanion/maps/"
      : "https://ameliah.art/cubepanion/maps/";
  private static EggWarsMapAPI instance;
  private final HashMap<String, LoadedEggWarsMap> convertedEggWarsMaps = new HashMap<>();

  private EggWarsMapAPI() {
    if (instance != null) {
      throw new RuntimeException("Class already initialized");
    }
    instance = this;
  }

  public static EggWarsMapAPI getInstance() {
    return instance;
  }

  public static void Init() {
    instance = new EggWarsMapAPI();
  }

  public void loadMaps() {
    getAllEggWarsMaps()
        .whenComplete((eggWarsMaps, throwable) -> {
          if (throwable != null) {
            LOGGER.error(getClass(), throwable, "Could not load EggWars maps.");
            return;
          }

          for (EggWarsMapAPI.EggWarsMap map : eggWarsMaps) {
            LoadedEggWarsMap eggWarsMap;
            try {
              eggWarsMap = fromAPIMap(map);
            } catch (Exception e) {
              LOGGER.warn(getClass(), e, "Could not convert EggWars map: " + map.map_name());
              continue;
            }
            if (eggWarsMap != null) {
              convertedEggWarsMaps.put(map.map_name().toLowerCase(), eggWarsMap);
            } else {
              LOGGER.warn(getClass(), "Could not convert EggWars map: " + map.map_name());
            }
          }
        })
        .exceptionally(throwable -> {
          LOGGER.error(getClass(), throwable, "Could not load EggWars maps.");
          return null;
        });
  }

  public LoadedEggWarsMap getEggWarsMapFromCache(String name) {
    return convertedEggWarsMaps.get(name.toLowerCase());
  }

  public HashMap<String, LoadedEggWarsMap> getConvertedEggWarsMaps() {
    return convertedEggWarsMaps;
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
    ;
    return makeRequest(baseURL, EggWarsMap[].class);
  }

  /**
   * Get EggWars map by name
   *
   * @param name map name
   * @return EggWars map
   */
  public CompletableFuture<EggWarsMap> getEggWarsMap(String name) {
    String url = baseURL + name;
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
