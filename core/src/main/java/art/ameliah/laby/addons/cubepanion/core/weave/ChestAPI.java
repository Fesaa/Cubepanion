package art.ameliah.laby.addons.cubepanion.core.weave;

import static art.ameliah.laby.addons.cubepanion.core.weave.Utils.makeRequest;

import art.ameliah.laby.addons.cubepanion.core.utils.LOGGER;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import javax.inject.Singleton;


@Singleton
public class ChestAPI {

  private static final String baseURL = System.getenv("CUBEPANION_DEV") != null
      ? "http://127.0.0.1/cubepanion/chests"
      : "https://ameliah.art/cubepanion/chests";
  private static ChestAPI instance;
  public List<ChestLocation> chestLocations = new ArrayList<>();
  private String Season = "";

  private ChestAPI() {
    if (instance != null) {
      throw new RuntimeException("Class already initialized");
    }
    instance = this;
  }

  public static ChestAPI getInstance() {
    return instance;
  }

  public static void Init() {
    instance = new ChestAPI();
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
    loadChestLocations(null);
  }

  public void loadChestLocations(Consumer<ChestLocation[]> consumer) {
    getCurrentChestLocations()
        .whenComplete((chestLocations, throwable) -> {
          if (throwable != null) {
            LOGGER.error(getClass(), throwable, "Could not load chest locations");
            return;
          }
          if (chestLocations == null) {
            LOGGER.error(getClass(), "Could not load chest locations. chestLocations is null");
            return;

          }
          setChestLocations(List.of(chestLocations));
        }).exceptionally(throwable -> {
          LOGGER.error(getClass(), throwable, "Could not load chest locations");
          return null;
        })
        .thenApplyAsync((chestLocations) -> {
          if (consumer != null) {
            consumer.accept(chestLocations);
          }
          return chestLocations;
        });
  }

  public void loadSeason() {
    loadSeason(null);
  }

  public void loadSeason(Consumer<String[]> consumer) {
    getSeasons(SeasonType.RUNNING)
        .whenComplete((seasons, throwable) -> {
          if (throwable != null) {
            LOGGER.error(getClass(), throwable, "Could not update Cubepanion#season");
            return;
          }
          if (seasons != null && seasons.length > 0) {
            setSeason(seasons[0]);
          } else {
            setSeason("");
          }
        }).exceptionally(throwable -> {
          LOGGER.error(getClass(), throwable, "Could not load chest locations");
          return null;
        })
        .thenApplyAsync((seasons) -> {
          if (consumer != null) {
            consumer.accept(seasons);
          }
          return seasons;
        });
  }


  /**
   * Requests chests locations for the current running season
   *
   * @return Array of ChestLocation's
   */
  public CompletableFuture<ChestLocation[]> getCurrentChestLocations() {
    String url = baseURL + "/";
    return makeRequest(url, ChestLocation[].class);
  }

  /**
   * Get all chest locations for a specific season
   *
   * @param season The Season the request the chests for
   * @return Array of ChestLocation's
   */
  public CompletableFuture<ChestLocation[]> getChestLocationsForSeason(String season) {
    String url = baseURL + "/" + season;
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
    String url = baseURL + "/seasons/" + seasonType.bool();
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
