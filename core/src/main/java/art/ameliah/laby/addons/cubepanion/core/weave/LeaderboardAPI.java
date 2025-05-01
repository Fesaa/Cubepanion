package art.ameliah.laby.addons.cubepanion.core.weave;

import static art.ameliah.laby.addons.cubepanion.core.weave.Utils.makeRequest;

import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import javax.inject.Singleton;
import net.labymod.api.util.io.web.request.Request;
import net.labymod.api.util.io.web.request.Request.Method;
import org.jetbrains.annotations.Nullable;


@Singleton
public class LeaderboardAPI {

  private static final String baseURL = System.getenv("CUBEPANION_DEV") != null
      ? "http://127.0.0.1/cubepanion/leaderboard"
      : "https://ameliah.art/cubepanion/leaderboard";

  private static LeaderboardAPI instance;

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

  /**
   * Submit a new leaderboard
   *
   * @param uuid    Submitters uuid
   * @param game    name
   * @param entries LeaderboardRows
   * @return StatusCode or Error
   */
  public CompletableFuture<Integer> submitLeaderboard(UUID uuid, APIGame game,
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
          GamesAPI.I().getNotNullGame(row.get("game").getAsString()),
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
      APIGame game) {
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
      APIGame game, int low, int up) {
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
   * Leaderboard Row
   *
   * @param game     Game's leaderboards
   * @param player   name
   * @param position int
   * @param score    int
   * @param texture string
   * @param unix     submission unix time stamp
   */
  public record LeaderboardRow(APIGame game, String player, int position, int score, @Nullable String texture, int unix) {

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
