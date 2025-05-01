package art.ameliah.laby.addons.cubepanion.core.weave;

import art.ameliah.laby.addons.cubepanion.core.utils.LOGGER;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import javax.inject.Singleton;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static art.ameliah.laby.addons.cubepanion.core.weave.Utils.makeRequest;

@Singleton
public class GamesAPI {

  private static final String baseURL = System.getenv("CUBEPANION_DEV") != null
      ? "http://127.0.0.1/cubepanion/games"
      : "https://ameliah.art/cubepanion/games";

  private static GamesAPI instance;
  private final HashMap<String, APIGame> converter = new HashMap<>();

  private final Type autoVoteMapTypeToken = new TypeToken<HashMap<String, AutoVoteConfig>>() {}.getType();
  private Map<String, AutoVoteConfig> configs = new HashMap<>();

  private GamesAPI() {
    if (instance != null) {
      throw new IllegalStateException("Already instantiated");
    }
    instance = this;
  }

  public static GamesAPI I() {
    return instance;
  }

  public static void Init() {
    instance = new GamesAPI();
    instance.load();
  }

  private void load() {
    makeRequest(baseURL + "/autoVote/config", autoVoteMapTypeToken)
        .handleAsync((configs, throwable) -> {
          if (throwable != null) {
            LOGGER.error(getClass(), "Failed to load autovote config", throwable);
            return null;
          }

          this.configs = (Map<String, AutoVoteConfig>) configs;
          LOGGER.debug(getClass(), "Loaded ", this.configs.size(), " auto vote configs");
          return null;
        });
  }

  public void loadGames() {
    loadGames(null);
  }

  public void loadGames(Consumer<APIGame[]> consumer) {
    // Players may be on non-active games, load all of them so ensure /lb commands
    // can display them correctly
    String url = String.format("%s/false", baseURL);
    CompletableFuture<JsonArray> completableFuture = makeRequest(url, JsonArray.class);
    completableFuture
        .handleAsync((leaderboards, throwable) -> {
          if (throwable != null) {
            LOGGER.error(getClass(), throwable, "Could not load leaderboards");
            return new APIGame[]{};
          }

          APIGame[] array = new APIGame[leaderboards.size()];
          for (int i = 0; i < leaderboards.size(); i++) {
            JsonObject obj = leaderboards.get(i).getAsJsonObject();
            APIGame game = new APIGame(obj.get("game").getAsString(),
                obj.get("display_name").getAsString(), true, obj.get("score_type").getAsString());
            for (JsonElement aliasElement : obj.get("aliases").getAsJsonArray()) {
              converter.put(aliasElement.getAsString(), game);
            }
            converter.put(game.name(), game);
            converter.put(game.displayName(), game);

            array[i] = game;
          }
          return array;
        })
        .exceptionally(throwable -> {
          LOGGER.error(getClass(), throwable, "Error while loading leaderboards");
          return  new APIGame[]{};
        }).thenApplyAsync(leaderboards -> {
          if (consumer != null) {
            consumer.accept(leaderboards);
          }
          return null;
        });
  }

  /**
   * Tries getting the Leaderboard class for a game
   *
   * @param game Can be the display name, name or an alias
   * @return Leaderboard or null
   */
  public @Nullable APIGame getGame(String game) {
    return this.converter.get(game);
  }

  /**
   * Tries getting the Leaderboard class for a game, but null safe
   *
   * @param game Can be the display name, name or an alias
   * @return Leaderboard or Leaderboard.UNKNOWN
   */
  public @NotNull APIGame getNotNullGame(String game) {
    APIGame APIGame = this.converter.get(game);
    if (APIGame == null) {
      return art.ameliah.laby.addons.cubepanion.core.weave.APIGame.UNKNOWN;
    }
    return APIGame;
  }

  /**
   * Get all leaderboards with their associated aliases
   *
   * @return Hashmap
   */
  public HashMap<APIGame, Set<String>> getAliases() {
    HashMap<APIGame, Set<String>> aliasesMap = new HashMap<>(this.converter.values().size());
    this.converter.forEach(
        (key, value) -> aliasesMap.computeIfAbsent(value, k -> new HashSet<>()).add(key));
    return aliasesMap;
  }

  public record AutoVoteConfig(int hotbar_index, List<AutoVoteOption> options) {}

  public record AutoVoteOption(String game, String choice_translation, int choice_index, AutoVoteType type, String[] translations) {}

  public enum AutoVoteType {
    TwoOptions,
    ThreeOptions,
    FourOptions,
    FiveOptions;
  }


}
