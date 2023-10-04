package org.cubepanion.core.weave;

import static org.cubepanion.core.weave.Utils.makeRequest;

import java.util.concurrent.CompletableFuture;


/**
 * Interaction class for ChestAPI
 */
public class ChestAPI {

  private final String baseURL;


  /**
   * @param url base API url
   */
  protected ChestAPI(String url) {
    this.baseURL = url;
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
