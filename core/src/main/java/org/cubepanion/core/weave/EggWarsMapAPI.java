package org.cubepanion.core.weave;

import javax.inject.Singleton;

import static org.cubepanion.core.weave.Utils.makeRequest;

import java.util.concurrent.CompletableFuture;

@Singleton
public class EggWarsMapAPI {

  private static EggWarsMapAPI instance;

  public static EggWarsMapAPI getInstance() {
    return instance;
  }

  private static final String baseURL = "https://ameliah.art/cubepanion_api";

  public EggWarsMapAPI() {
    if (instance != null) {
      throw new RuntimeException("Class already initialized");
    }
    instance = this;
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
