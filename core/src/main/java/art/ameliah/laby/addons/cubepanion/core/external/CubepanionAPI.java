package art.ameliah.laby.addons.cubepanion.core.external;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.listener.internal.SessionTracker;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import art.ameliah.laby.addons.cubepanion.core.utils.gamemaps.AbstractGameMap;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.inject.Singleton;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.notification.Notification;
import net.labymod.api.notification.Notification.Type;
import net.labymod.api.util.GsonUtil;
import net.labymod.api.util.io.web.request.Request;
import net.labymod.api.util.io.web.request.Request.Method;
import net.labymod.api.util.logging.Logging;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Singleton
public class CubepanionAPI {

  private final Logging log = Logging.create(CubepanionAPI.class.getSimpleName());
  private final TypeToken<List<Game>> gamesToken = new TypeToken<>() {};
  private final TypeToken<List<LeaderboardRow>> lbRowsToken = new TypeToken<>() {};
  private final TypeToken<List<ChestLocation>> chestLocationToken = new TypeToken<>() {};
  private final TypeToken<List<GameMap>> gameMapsToken = new TypeToken<>() {};

  private final String baseUrl = System.getenv("DEV") == null ?
      "https://cubepanion.ameliah.art/api" : System.getenv("DEV") + "/api";
  private final String baseUrlv2 = System.getenv("DEV") == null ?
      "https://cubepanion.ameliah.art/api/v2" : System.getenv("DEV") + "/api/v2";

  private final Map<String, Game> games = new HashMap<>();
  private final Map<Integer, Game> gameById = new HashMap<>();
  private final List<ChestLocation> chestLocations = new ArrayList<>();
  private final HashMap<Integer, HashMap<String, AbstractGameMap>> convertedGameMaps = new HashMap<>();
  private LeaderboardConfiguration leaderboardConfiguration = new LeaderboardConfiguration(false, 200, 100);

  public CompletableFuture<Boolean> loadGames(boolean toastOnSuccess) {
    return retryOnceWithToast(
        this::getGames,
        games -> {
          if (games == null) {
            return;
          }
          for (var game : games) {
            this.gameById.put(game.id(), game);
            this.games.put(game.name(), game);
            this.games.put(game.displayName(), game);
            game.aliases().forEach(a -> this.games.put(a, game));
          }
          log.info("Loaded {} games", games.size());
        },
        games -> ToastDescription.of(
            "cubepanion.notifications.reload.games.title",
            "cubepanion.notifications.reload.games.success",
            Component.text(games.size())),
        ex -> ToastDescription.of(
            "cubepanion.notifications.reload.games.title",
            "cubepanion.notifications.reload.games.failure"),
        toastOnSuccess);
  }

  public CompletableFuture<Boolean> loadChestLocations(boolean toastOnSuccess) {
    return retryOnceWithToast(
        this::loadChestLocations,
        chestLocations -> {
          if (chestLocations == null) {
            return;
          }
          this.chestLocations.clear();
          this.chestLocations.addAll(chestLocations);
          log.info("Loaded {} chest locations", this.chestLocations.size());
        },
        cl -> ToastDescription.of(
            "cubepanion.notifications.reload.chests.title",
            "cubepanion.notifications.reload.chests.success",
            Component.text(cl.size())),
        ex -> ToastDescription.of(
            "cubepanion.notifications.reload.chests.title",
            "cubepanion.notifications.reload.chests.failure"),
        toastOnSuccess);
  }

  public CompletableFuture<Boolean> loadGameMaps(boolean toastOnSuccess) {
    return retryOnceWithToast(
        this::loadGameMaps,
        gameMaps -> {
          if (gameMaps == null) {
            log.warn("Failed to load game maps, some features may not work correctly");
            return;
          }

          this.convertedGameMaps.clear();
          for (var gameMap : gameMaps) {
            var convertedMap = AbstractGameMap.constructFromAPI(gameMap);

            var map = this.convertedGameMaps.putIfAbsent(gameMap.gameId(), new HashMap<>());
            if (map == null) {
              continue;
            }
            map.put(gameMap.mapName().toLowerCase(), convertedMap);
          }

          log.info("Loaded {} game-maps. {} total maps in the api", this.convertedGameMaps.size(), gameMaps.size());
        },
        maps -> ToastDescription.of(
            "cubepanion.notifications.reload.maps.title",
            "cubepanion.notifications.reload.maps.success",
            Component.text(maps.size())),
        ex -> ToastDescription.of(
            "cubepanion.notifications.reload.maps.title",
            "cubepanion.notifications.reload.maps.failure"),
            toastOnSuccess);
  }

  public CompletableFuture<Boolean> loadLeaderboardConfiguration(boolean toastOnSuccess) {
    return retryOnceWithToast(
        () -> get(this.baseUrlv2 + "/Leaderboard/config", LeaderboardConfiguration.class),
        config -> leaderboardConfiguration = config,
        ignored -> ToastDescription.of(
            "cubepanion.notifications.reload.leaderboard-config.title",
            "cubepanion.notifications.reload.leaderboard-config.success"
        ),
        ignored -> ToastDescription.of(
            "cubepanion.notifications.reload.leaderboard-config.title",
            "cubepanion.notifications.reload.leaderboard-config.failure"
            ),
        toastOnSuccess);
  }

  public void loadInitialData() {
    this.loadInitialData(false);
  }

  public void loadInitialData(boolean toastOnSuccess) {
    log.info("Loading initial data from {} & {}", this.baseUrl, this.baseUrlv2);

    this.loadGames(false)
        .handleAsync((success, ex) -> null)
        .thenComposeAsync(ignored -> this.loadChestLocations(toastOnSuccess))
        .handleAsync((success, ex) -> null)
        .thenComposeAsync(ignored -> this.loadGameMaps(toastOnSuccess))
        .handleAsync((success, ex) -> null)
        .thenComposeAsync(ignored -> this.loadLeaderboardConfiguration(toastOnSuccess))
        .whenCompleteAsync((success, ex) -> log.info("Finished loading initial data"));
  }

  public boolean hasMaps(CubeGame cubeGame) {
    var game = this.tryGame(cubeGame.getString());
    if (game == null) {
      return false;
    }

    return this.convertedGameMaps.containsKey(game.id());
  }

  @NotNull
  public LeaderboardConfiguration getLeaderboardConfiguration() {
    return this.leaderboardConfiguration;
  }

  @Nullable
  public AbstractGameMap currentMap() {
    var m = Cubepanion.get().getManager();
    return getGameMap(m.getDivision(), m.getMapName());
  }

  @Nullable
  public AbstractGameMap getGameMap(CubeGame cubeGame, String mapName) {
    var game = this.tryGame(cubeGame.getString());
    if (game == null) {
      return null;
    }

    var maps = this.convertedGameMaps.get(game.id());
    if (maps == null) {
      return null;
    }

    return maps.get(mapName.toLowerCase());
  }

  public List<ChestLocation> getChestLocations() {
    return this.chestLocations;
  }

  @NotNull
  public Map<String, Game> getGamesMap() {
    return new HashMap<>(games);
  }

  @Nullable
  public Game getGameById(int id) {
    return this.gameById.get(id);
  }

  @Nullable
  private Game getGame(String game) {
    return this.games.get(game);
  }

  @Nullable
  public Game getGame(CubeGame cubeGame) {
    return this.tryGame(cubeGame.getString());
  }

  @Nullable
  public Game getNotNullGame(String game) {
    return this.games.getOrDefault(game, Game.UNKNOWN);
  }

  @Nullable
  public Game tryGame(String game) {
    return this.getGame(game.replace(" ", "_").toLowerCase().trim());
  }

  public int totalGames() {
    return new HashSet<>(this.games.values()).size();
  }

  public int totalMaps() {
    var total = 0;
    for (var mapsMap : convertedGameMaps.values()) {
      total += mapsMap.size();
    }
    return total;
  }

  public CompletableFuture<Leaderboard> getLeaderboard(Game game, int lower, int upper) {
    return this.get(String.format("%s/Leaderboard/game/%s?lower=%s&upper=%s",
        this.baseUrlv2, game.name(), lower, upper), Leaderboard.class);
  }

  public CompletableFuture<Leaderboard> getLeaderboard(Game game) {
    return this.get(this.baseUrlv2+"/Leaderboard/game/"+game.name(), Leaderboard.class);
  }

  public CompletableFuture<PlayerLeaderboard> getPlayerLeaderboard(String name) {
    return this.get(this.baseUrlv2+"/Leaderboard/player/"+name, PlayerLeaderboard.class);
  }

  public CompletableFuture<List<ChestLocation>> loadChestLocations() {
    return this.get(this.baseUrl+"/Chests", chestLocationToken);
  }

  public CompletableFuture<List<GameMap>> loadGameMaps() {
    return this.get(this.baseUrlv2+"/Maps", gameMapsToken);
  }

  public CompletableFuture<List<Game>> getGames() {
    return this.get(this.baseUrlv2+"/Games", gamesToken);
  }

  public CompletableFuture<Void> submit(Game game, List<LeaderboardRow> entries) {
    var submission = new Submission(SessionTracker.get().uuid().toString(), game.id(), entries);

    var player = Laby.labyAPI().minecraft().getClientPlayer();
    if (player == null) {
      return CompletableFuture.completedFuture(null);
    }

    CompletableFuture<Void> future = new CompletableFuture<>();

    Request.ofString()
        .url(this.baseUrlv2+"/Leaderboard/new")
        .method(Method.POST)
        .json(submission)
        .async()
        .execute(c -> {
          if (c.hasException()) {
            future.completeExceptionally(c.exception());
            return;
          }

          if (c.getStatusCode() != 202) {
            future.completeExceptionally(new Exception("Server returned status code " + c.getStatusCode()));
          }

          future.complete(null);
        });

    return future;
  }

  public CompletableFuture<List<LeaderboardRow>> batch(Game game, List<String> players) {
    var request = new BatchRequest(game.name(), players);

    CompletableFuture<List<LeaderboardRow>> future = new CompletableFuture<>();
    Request.ofString()
        .url(this.baseUrl+"/Leaderboard/batch")
        .method(Method.POST)
        .json(request)
        .async()
        .execute(c -> {
          if (c.hasException()) {
            future.completeExceptionally(c.exception());
            return;
          }
          if (c.getStatusCode() != 200) {
            future.completeExceptionally(new Exception("Server returned status code " + c.getStatusCode()));
            return;
          }

          try {
            future.complete((new Gson()).fromJson(c.get(), this.lbRowsToken));
          } catch (JsonSyntaxException exp) {
            future.completeExceptionally(exp);
          }
        });

    return future;
  }

  private <T> CompletableFuture<T> get(String url, Class<T> clazz) {
    CompletableFuture<T> future = new CompletableFuture<>();

    Request.ofString()
        .url(url)
        .async()
        .execute(c -> {
          if (c.hasException()) {
            log.debug("Failed to make get request to {}, {}", url, c.exception());
            future.completeExceptionally(c.exception());
          }

          if (c.getStatusCode() != 200) {
            log.debug("Failed to make get request to {}, {}", url, c.getStatusCode());
            future.completeExceptionally(new IllegalArgumentException("CubepanionAPI returned a non 200 status code: " + c.getStatusCode()));
          }

          if (c.isEmpty()) {
            log.debug("CubepanionAPI returned an empty response");
            future.complete(null);
          }

          try {
            future.complete(GsonUtil.DEFAULT_GSON.fromJson(c.get(), clazz));
          } catch (JsonSyntaxException exp) {
            future.completeExceptionally(exp);
          }
        });

    return future;
  }

  private <T> CompletableFuture<T> get(String url, TypeToken<T> token) {
    CompletableFuture<T> future = new CompletableFuture<>();

    Request.ofString()
        .url(url)
        .async()
        .execute(c -> {
          if (c.hasException()) {
            log.debug("Failed to make get request to {}, {}", url, c.exception());
            future.completeExceptionally(c.exception());
            return;
          }

          if (c.getStatusCode() != 200) {
            log.debug("Failed to make get request to {}, {}", url, c.getStatusCode());
            future.completeExceptionally(new IllegalArgumentException("CubepanionAPI returned a non 200 status code: " + c.getStatusCode()));
            return;
          }

          if (c.isEmpty()) {
            log.debug("CubepanionAPI returned an empty response");
            future.complete(null);
            return;
          }

          try {
            future.complete(GsonUtil.DEFAULT_GSON.fromJson(c.get(), token));
          } catch (JsonSyntaxException exp) {
            future.completeExceptionally(exp);
          }
        });

    return future;
  }

  private static final Duration RETRY_DELAY = Duration.ofSeconds(2);

  public <T> CompletableFuture<Boolean> retryOnceWithToast(
      Supplier<CompletableFuture<T>> loader,
      Consumer<T> onSuccess,
      Function<T, ToastDescription> successToast,
      Function<Throwable, ToastDescription> failureToast,
      boolean toastOnSuccess
  ) {
    return loader.get()
        .exceptionallyComposeAsync(ex -> {
          log.warn("Initial load failed, retrying in {}", RETRY_DELAY, ex);
          return CompletableFuture
              .supplyAsync(() -> null, CompletableFuture.delayedExecutor(
                  RETRY_DELAY.toMillis(), TimeUnit.MILLISECONDS))
              .thenComposeAsync(ignored -> loader.get());
        })
        .handleAsync((result, ex) -> {
          if (ex != null) {
            log.error("Failed to load data after retry", ex);
            pushToast(failureToast.apply(ex));
            return false;
          }

          try {
            onSuccess.accept(result);
          } catch (Exception e) {
            log.error("onSuccess callback threw while processing loaded data", e);
            pushToast(failureToast.apply(e));
            return false;
          }

          if (toastOnSuccess) {
            pushToast(successToast.apply(result));
          }

          return true;
        });
  }

  private static void pushToast(ToastDescription description) {
    Laby.labyAPI().notificationController().push(Notification.builder()
        .title(Component.translatable(description.titleKey(), description.titleArgs()))
        .text(Component.translatable(description.textKey(), description.textArgs()))
        .type(Type.SYSTEM)
        .build());
  }

  public record ToastDescription(String titleKey, Component[] titleArgs, String textKey, Component[] textArgs) {

    public static ToastDescription of(String titleKey, String textKey) {
      return new ToastDescription(titleKey, new Component[0], textKey, new Component[0]);
    }

    public static ToastDescription of(String titleKey, String textKey, Component... textArgs) {
      return new ToastDescription(titleKey, new Component[0], textKey, textArgs);
    }
  }

  private static CubepanionAPI instance;

  private CubepanionAPI() {
  }

  public static CubepanionAPI I() {
    return CubepanionAPI.instance;
  }

  public static void Init() {
    if (instance != null) {
      throw new RuntimeException("Class already initialized");
    }
    CubepanionAPI.instance = new CubepanionAPI();
  }

}
