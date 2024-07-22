package art.ameliah.laby.addons.cubepanion.core.weave;

import static art.ameliah.laby.addons.cubepanion.core.weave.Utils.makeRequest;

import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import art.ameliah.laby.addons.cubepanion.core.utils.LOGGER;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import javax.inject.Singleton;
import net.labymod.api.util.io.web.request.Request;
import net.labymod.api.util.io.web.request.Request.Method;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Singleton
public class LeaderboardAPI {

  private static final String baseURL = System.getenv("CUBEPANION_DEV") != null
      ? "http://127.0.0.1/cubepanion/leaderboard"
      : "https://ameliah.art/cubepanion/leaderboard";

  private static final String gamesBaseURL = System.getenv("CUBEPANION_DEV") != null
      ? "http://127.0.0.1/cubepanion/games"
      : "https://ameliah.art/cubepanion/games";

  private static LeaderboardAPI instance;
  private final HashMap<String, Leaderboard> converter = new HashMap<>();

  private LeaderboardAPI() {
    if (instance != null) {
      throw new RuntimeException("Class already initialized");
    }
    instance = this;
  }

  public static LeaderboardAPI getInstance() {
    return instance;
  }

  public static void Init() {
    instance = new LeaderboardAPI();
  }

  public void loadLeaderboards() {
    // Players may be on non-active games, load all of them so ensure /lb commands
    // can display them correctly
    String url = String.format("%s/false", gamesBaseURL);
    CompletableFuture<JsonArray> completableFuture = makeRequest(url, JsonArray.class);
    completableFuture
        .whenComplete((leaderboards, throwable) -> {
          if (throwable != null) {
            LOGGER.error(getClass(), throwable, "Could not load leaderboards");
            return;
          }

          for (JsonElement el : leaderboards) {
            JsonObject obj = el.getAsJsonObject();
            Leaderboard game = new Leaderboard(obj.get("game").getAsString(),
                obj.get("display_name").getAsString(), true, obj.get("score_type").getAsString());
            for (JsonElement aliasElement : obj.get("aliases").getAsJsonArray()) {
              converter.put(aliasElement.getAsString(), game);
            }
            converter.put(game.name(), game);
            converter.put(game.displayName(), game);
          }
        })
        .exceptionally(throwable -> {
          LOGGER.error(getClass(), throwable, "Error while loading leaderboards");
          return new JsonArray();
        });
  }

  /**
   * Submit a new leaderboard
   *
   * @param uuid    Submitters uuid
   * @param game    name
   * @param entries LeaderboardRows
   * @return StatusCode or Error
   */
  public CompletableFuture<Integer> submitLeaderboard(UUID uuid, Leaderboard game,
      Set<LeaderboardRow> entries) {
    if (entries.size() != 200) {
      return CompletableFuture.failedFuture(
          new WeaveException("entries set must contain exactly 200 entries."));
    }
    if (!game.active()) {
      return CompletableFuture.failedFuture(
          new WeaveException("Game is not active, cannot submit"));
    }

    JsonObject main = new JsonObject();
    main.addProperty("uuid", uuid.toString());
    main.addProperty("unix_time_stamp", System.currentTimeMillis());
    main.addProperty("game", game.displayName());
    JsonArray cachedEntries = new JsonArray(200);
    for (LeaderboardRow entry : entries) {
      cachedEntries.add(entry.getAsJsonElement());
    }
    main.add("entries", cachedEntries);

    CompletableFuture<Integer> future = new CompletableFuture<>();
    Request.ofString()
        .url(baseURL + "/")
        .method(Method.POST)
        .addHeader("Content-Type", "application/json")
        .json(main)
        .async()
        .execute(c -> {
          WeaveException e = WeaveException.fromResponse(c, 202, false);
          if (e != null) {
            throw new RuntimeException(e);
          }
          future.complete(202);
        });
    return future;
  }

  private LeaderboardRow[] jsonArrayToArray(JsonArray array) {
    List<LeaderboardRow> rows = new ArrayList<>();

    for (JsonElement el : array) {
      JsonObject row = el.getAsJsonObject();
      rows.add(new LeaderboardRow(
          // Making sure that no null values are passed as the addon later assumes they're known
          // The leaderboard will be assumed unknown if null
          getNotNullLeaderboard(row.get("game").getAsString()),
          row.get("player").getAsString(),
          row.get("position").getAsInt(),
          row.get("score").getAsInt(),
          row.has("texture") ? row.get("texture").getAsString() : null,
          row.get("unix_time_stamp").getAsInt()
      ));
    }
    return rows.toArray(new LeaderboardRow[0]);
  }

  private CompletableFuture<LeaderboardRow[]> leaderBoardRowRequest(String url) {
    return leaderBoardRowRequest(url, null);
  }

  private CompletableFuture<LeaderboardRow[]> leaderBoardRowRequest(String url, Object json) {
    CompletableFuture<LeaderboardRow[]> completableFuture = new CompletableFuture<>();

    Request<String> request = Request.ofString()
        .url(url)
        .async();

    if (json != null) {
      request.json(json);
    }

    request
        .execute(c -> {
          WeaveException e = WeaveException.fromResponse(c, 200, true);
          if (e != null) {
            completableFuture.completeExceptionally(e);
            return;
          }

          try {
            JsonArray array = (new Gson()).fromJson(c.get(), JsonArray.class);
            completableFuture.complete(jsonArrayToArray(array));
          } catch (JsonSyntaxException exp) {
            completableFuture.completeExceptionally(exp);
          }
        });
    return completableFuture;
  }

  public CompletableFuture<LeaderboardRow[]> getLeaderboardForPlayers(CubeGame game,
      String[] players) {
    String url = String.format("%s/batch", baseURL);

    JsonObject main = new JsonObject();
    main.addProperty("game", game.getString());
    JsonArray playersArray = new JsonArray();
    for (String player : players) {
      playersArray.add(player);
    }
    main.add("players", playersArray);

    return leaderBoardRowRequest(url, main);
  }

  /**
   * Retrieve all leaderboards for a player
   *
   * @param player name
   * @return Array of LeaderboardRow's
   */
  public CompletableFuture<LeaderboardRow[]> getLeaderboardsForPlayer(String player) {
    String url = String.format("%s/player/%s", baseURL, player);
    return leaderBoardRowRequest(url);
  }

  /**
   * Retrieve all leaderboardRows for a game
   *
   * @param game name
   * @return Array of LeaderboardRow's
   */
  public CompletableFuture<LeaderboardRow[]> getGameLeaderboard(
      Leaderboard game) {
    return getGameLeaderboard(game, 1, 200);
  }

  /**
   * Retrieve all leaderboardRows for a game between bounds
   *
   * @param game name
   * @param low  lower bound
   * @param up   upper bound (must be higher than low)
   * @return Array of LeaderboardRow's
   */
  public CompletableFuture<LeaderboardRow[]> getGameLeaderboard(
      Leaderboard game, int low, int up) {
    if (up < low) {
      return CompletableFuture.failedFuture(
          new WeaveException("Upper bound must be higher than the lower bound"));
    }

    if (low == 1 && up == 200) {
      return getGameLeaderboard(game);
    }

    String url = String.format("%s/game/%s/bounded?lower=%d&upper=%d",
        baseURL,
        game.displayName().replace(" ", "%20"), low, up);
    return leaderBoardRowRequest(url);
  }

  /**
   * Tries getting the Leaderboard class for a game
   *
   * @param game Can be the display name, name or an alias
   * @return Leaderboard or null
   */
  public @Nullable Leaderboard getLeaderboard(String game) {
    return this.converter.get(game);
  }

  /**
   * Tries getting the Leaderboard class for a game, but null safe
   *
   * @param game Can be the display name, name or an alias
   * @return Leaderboard or Leaderboard.UNKNOWN
   */
  public @NotNull Leaderboard getNotNullLeaderboard(String game) {
    Leaderboard leaderboard = this.converter.get(game);
    if (leaderboard == null) {
      return Leaderboard.UNKNOWN;
    }
    return leaderboard;
  }

  /**
   * Get all leaderboards with their associated aliases
   *
   * @return Hashmap
   */
  public HashMap<Leaderboard, Set<String>> getAliases() {
    HashMap<Leaderboard, Set<String>> aliasesMap = new HashMap<>(this.converter.values().size());
    this.converter.forEach(
        (key, value) -> aliasesMap.computeIfAbsent(value, k -> new HashSet<>()).add(key));
    return aliasesMap;
  }

  /**
   * @param name        internal name
   * @param displayName display name
   * @param active      if the leaderboard is active (and can be submitted to)
   */
  public record Leaderboard(String name, String displayName, boolean active, String scoreType) {

    public static Leaderboard UNKNOWN = new Leaderboard("unknown", "Unknown", false, "Unknown");
  }

  /**
   * Leaderboard Row
   *
   * @param game     Game's leaderboards
   * @param player   name
   * @param position int
   * @param score    int
   * @param texture string
   * @param unix     submission unix time stamp
   */
  public record LeaderboardRow(Leaderboard game, String player, int position, int score, @Nullable String texture, int unix) {

    JsonElement getAsJsonElement() {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("game", game.displayName());
      jsonObject.addProperty("player", player);
      jsonObject.addProperty("position", position);
      if (texture != null) {
        jsonObject.addProperty("texture", texture);
      }
      jsonObject.addProperty("score", score);
      return jsonObject;
    }
  }
}
