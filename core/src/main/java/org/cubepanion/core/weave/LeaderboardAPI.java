package org.cubepanion.core.weave;

import static org.cubepanion.core.weave.Utils.makeRequest;

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
import net.labymod.api.util.io.web.request.Request;
import net.labymod.api.util.io.web.request.Request.Method;
import org.cubepanion.core.utils.LOGGER;
import org.jetbrains.annotations.Nullable;
import javax.inject.Singleton;


@Singleton
public class LeaderboardAPI {

  private static LeaderboardAPI instance;

  public static LeaderboardAPI getInstance() {
    return instance;
  }

  private static final String baseURL = "https://ameliah.art/cubepanion_api";

  private final HashMap<String, Leaderboard> converter = new HashMap<>();

  public LeaderboardAPI() {
    if (instance != null) {
      throw new RuntimeException("Class already initialized");
    }
    instance = this;
  }

  public void loadLeaderboards() {
    String url2 = String.format("%s/leaderboard_api/games/true", baseURL);
    CompletableFuture<JsonArray> completableFuture = makeRequest(url2, JsonArray.class);
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

    String url = baseURL + "/leaderboard_api";

    CompletableFuture<Integer> future = new CompletableFuture<>();

    Request.ofString()
        .url(url)
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
          this.converter.get(row.get("game").getAsString()),
          row.get("player").getAsString(),
          row.get("position").getAsInt(),
          row.get("score").getAsInt(),
          row.get("unix_time_stamp").getAsInt()
      ));
    }
    return rows.toArray(new LeaderboardRow[0]);
  }

  private CompletableFuture<LeaderboardRow[]> leaderBoardRowRequest(String url) {
    CompletableFuture<LeaderboardRow[]> completableFuture = new CompletableFuture<>();

    Request.ofString()
        .url(url)
        .async()
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

  /**
   * Retrieve all leaderboards for a player
   *
   * @param player name
   * @return Array of LeaderboardRow's
   */
  public CompletableFuture<LeaderboardRow[]> getLeaderboardsForPlayer(String player) {
    String url = String.format("%s/leaderboard_api/player/%s", baseURL, player);
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
    String url = String.format("%s/leaderboard_api/leaderboard/%s/bounded?lower=%d&upper=%d",
        baseURL,
        game.displayName().replace(" ", "%20"), low, up);
    return leaderBoardRowRequest(url);
  }

  /**
   * Tries getting the Leaderboard class for a game
   *
   * @param game Can be the display name, name or an alias
   * @return Leaderboard or Error wrapped in Result
   */
  public @Nullable Leaderboard getLeaderboard(String game) {
    return this.converter.get(game);
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

  }

  /**
   * Leaderboard Row
   *
   * @param game     Game's leaderboards
   * @param player   name
   * @param position int
   * @param score    int
   * @param unix     submission unix time stamp
   */
  public record LeaderboardRow(Leaderboard game, String player, int position, int score, int unix) {

    JsonElement getAsJsonElement() {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("game", game.displayName());
      jsonObject.addProperty("player", player);
      jsonObject.addProperty("position", position);
      jsonObject.addProperty("score", score);
      return jsonObject;
    }
  }
}
