package org.cubepanion.core.weave;

import org.cubepanion.core.utils.LOGGER;
import javax.inject.Singleton;

import static org.cubepanion.core.weave.Utils.makeRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@Singleton
public class ChestAPI {

  private static ChestAPI instance;

  public static ChestAPI getInstance() {
    return instance;
  }

  private static final String baseURL = "https://ameliah.art/cubepanion_api";

  private String Season = "";
  public List<ChestLocation> chestLocations = new ArrayList<>();


  public static void Init() {
    instance = new ChestAPI();
  }

  private ChestAPI() {
    if (instance != null) {
      throw new RuntimeException("Class already initialized");
    }
    instance = this;
  }

  public String getSeason() {
    return Season;
  }

  public void setSeason(String season) {
    Season = season;
  }

  public List<ChestLocation> getChestLocations() {
    return chestLocations;
  }

  public void setChestLocations(List<ChestLocation> chestLocations) {
    this.chestLocations = chestLocations;
  }

  public void loadChestLocations() {
    getCurrentChestLocations()
        .whenComplete((chestLocations, throwable) -> {
          if (throwable != null) {
            LOGGER.error(getClass(), throwable, "Could not load chest locations");
            return;
          }
          setChestLocations(List.of(chestLocations));
        });
  }

  public void loadSeason() {
    getSeasons(SeasonType.RUNNING)
        .whenComplete((seasons, throwable) -> {
          if (throwable != null) {
            LOGGER.error(getClass(), throwable, "Could not update Cubepanion#season");
            return;
          }
          if (seasons.length > 0) {
            setSeason(seasons[0]);
          } else {
            setSeason("");
          }
        });
  }


  /**
   * Requests chests locations for the current running season
   *
   * @return Array of ChestLocation's
   */
  public CompletableFuture<ChestLocation[]> getCurrentChestLocations() {
    String url = baseURL + "/chest_api/current";
    return makeRequest(url, ChestLocation[].class);
  }

  /**
   * Get all chest locations for a specific season
   *
   * @param season The Season the request the chests for
   * @return Array of ChestLocation's
   */
  public CompletableFuture<ChestLocation[]> getChestLocationsForSeason(String season) {
    String url = baseURL + "/chest_api/season/" + season;
    return makeRequest(url, ChestLocation[].class);
  }

  /**
   * Get all seasons
   *
   * @return Array of seasons (String)
   */
  public CompletableFuture<String[]> getSeasons() {
    return getSeasons(SeasonType.ALL);
  }

  /**
   * Get seasons bounded by the request type
   *
   * @param seasonType Request type
   * @return Array of seasons (String)
   */
  public CompletableFuture<String[]> getSeasons(SeasonType seasonType) {
    String url = baseURL + "/chest_api/seasons/" + seasonType.bool();
    return makeRequest(url, String[].class);
  }

  /**
   * Request type for seasons
   */
  public enum SeasonType {
    /**
     * Active season
     */
    RUNNING,
    /**
     * All seasons
     */
    ALL;

    private String bool() {
      if (this == SeasonType.RUNNING) {
        return "true";
      }
      return "false";
    }
  }

  /**
   * Location of a chest
   *
   * @param season_name Season the chest is connected to
   * @param x           x-coord
   * @param y           y-coord
   * @param z           z-coord
   */
  public record ChestLocation(String season_name, int x, int y, int z) {

  }
}