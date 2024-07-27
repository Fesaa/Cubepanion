package art.ameliah.laby.addons.cubepanion.core.weave;

import static art.ameliah.laby.addons.cubepanion.core.utils.Utils.fromAPIMap;
import static art.ameliah.laby.addons.cubepanion.core.weave.Utils.makeRequest;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.managers.CubepanionManager;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import art.ameliah.laby.addons.cubepanion.core.utils.LOGGER;
import art.ameliah.laby.addons.cubepanion.core.utils.gamemaps.base.LoadedGameMap;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import javax.inject.Singleton;
import net.labymod.api.client.component.Component;

@Singleton
public class GameMapAPI {

  private static final String baseURL = System.getenv("CUBEPANION_DEV") != null
      ? "http://127.0.0.1/cubepanion/maps/"
      : "https://ameliah.art/cubepanion/maps/";
  private static GameMapAPI instance;
  private final HashMap<CubeGame, HashMap<String, LoadedGameMap>> convertedGameMaps = new HashMap<>();

  private GameMapAPI() {
    if (instance != null) {
      throw new RuntimeException("Class already initialized");
    }
    instance = this;
  }

  public static GameMapAPI getInstance() {
    return instance;
  }

  public static void Init() {
    instance = new GameMapAPI();
  }

  public void loadMaps() {
    getAllGameMaps()
        .whenComplete((gameMap, throwable) -> {
          if (throwable != null) {
            LOGGER.error(getClass(), throwable, "Could not load EggWars maps.");
            return;
          }

          for (GameMap map : gameMap) {
            LoadedGameMap loadedGameMap;
            try {
              loadedGameMap = fromAPIMap(map);
            } catch (Exception e) {
              LOGGER.warn(getClass(), e, "Could not convert EggWars map: " + map.map_name());
              continue;
            }
            if (loadedGameMap != null) {
              convertedGameMaps
                  .computeIfAbsent(loadedGameMap.getGame(), (k) -> new HashMap<>())
                  .put(loadedGameMap.getName().toLowerCase(), loadedGameMap);
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

  public LoadedGameMap getCurrentMap() {
    return this.getGameMapFromCache(Cubepanion.get().getManager());
  }

  public LoadedGameMap getGameMapFromCache(CubepanionManager manager) {
    return this.getGameMapFromCache(manager.getDivision(), manager.getMapName());
  }

  public LoadedGameMap getGameMapFromCache(CubeGame game, String name) {
    return convertedGameMaps.getOrDefault(game, new HashMap<>()).get(name.toLowerCase());
  }

  public int size() {
    return convertedGameMaps.values().stream().map(HashMap::size).reduce(0, Integer::sum);
  }

  public Component getAllMapNames(CubeGame game) {
    Component out = Component.empty();

    for (String name : convertedGameMaps.getOrDefault(game, new HashMap<>()).keySet()) {
      out = out.append(Component.text(name))
          .append(Component.text(", "));
    }
    return out;
  }

  public boolean hasMaps(CubeGame game) {
    return convertedGameMaps.containsKey(game);
  }

  /**
   * Get all GameMaps maps
   *
   * @return all GameMaps maps
   */
  public CompletableFuture<GameMap[]> getAllGameMaps() {
    return makeRequest(baseURL, GameMap[].class);
  }


  /**
   * EggWars map
   */
  public record GameMap(String game, String unique_name, String map_name, String layout, int team_size,
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
