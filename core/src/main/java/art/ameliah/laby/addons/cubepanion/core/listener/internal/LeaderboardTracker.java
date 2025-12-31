package art.ameliah.laby.addons.cubepanion.core.listener.internal;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.accessors.CCItemStack;
import art.ameliah.laby.addons.cubepanion.core.external.CubepanionAPI;
import art.ameliah.laby.addons.cubepanion.core.external.Game;
import art.ameliah.laby.addons.cubepanion.core.external.LeaderboardRow;
import art.ameliah.laby.addons.cubepanion.core.utils.Colours;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.gui.screen.ScreenDisplayEvent;
import net.labymod.api.util.logging.Logging;
import org.jetbrains.annotations.Nullable;

public class LeaderboardTracker {

  private final Pattern pagePattern = Pattern.compile(".*\\((\\d+)/\\d+\\)");
  private static final String DIVIDER = ": ";

  private final Logging log = Logging.create(Cubepanion.class.getSimpleName());

  private final Cubepanion addon;

  private final Map<Game, Long> lastSubmit = new HashMap<>();

  private final Set<Integer> pages = new HashSet<>();
  private int currentPage = -1;

  private final Set<LeaderboardRow> rows = new HashSet<>(200);
  @Nullable
  private Game currentTrackingGame = null;

  public LeaderboardTracker(Cubepanion addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onScreenOpen(ScreenDisplayEvent e) {
    if (!this.addon.getManager().onCubeCraft() || !this.addon.getManager().getDivision().equals(CubeGame.LOBBY)) {
      return;
    }

    if (this.addon.getFunctionLink() == null) {
      return;
    }

    this.addon.getFunctionLink()
        .loadMenuItems(this::extractAndTrackTitle)
        .thenApplyAsync(this::enumerateItems)
        .thenComposeAsync(submit -> {
          if (!submit || this.currentTrackingGame == null) {
            return CompletableFuture.completedFuture(null);
          }

          var submittingFor = this.currentTrackingGame;
          var rows = this.rows.stream().toList();

          return CubepanionAPI.I()
              .submit(submittingFor, rows)
              .thenAcceptAsync(ignored -> this.onSuccess(submittingFor));
        })
        .exceptionallyAsync(ex -> {
          log.error("Failed to load or submit leaderboard", ex);
          return null;
        });
  }

  private void onSuccess(Game submittingFor) {
    Laby.labyAPI().minecraft().chatExecutor().displayClientMessage(
        Component.translatable(
            "cubepanion.messages.leaderboardAPI.success",
            Component.text(submittingFor.displayName())
        ).color(Colours.Success)
    );
    this.reset();
  }

  private boolean enumerateItems(List<CCItemStack> items) {
    if (items == null || items.isEmpty()) {
      return false;
    }

    var count = 0;
    for (var item : items) {
      if (item.isAir()) {
        continue;
      }

      if (!item.getAsItem().getIdentifier().getPath().equals("player_head")) {
        continue;
      }

      var row = this.parseLeaderboardRow(item);
      if (row == null) {
        // Profile is a player head as well
        continue;
      }

      this.rows.add(row);
      count++;
    }

    log.debug("Adding {} row from {} items for {}", count, items.size(), this.currentTrackingGame);
    return this.checkSubmit();
  }

  private LeaderboardRow parseLeaderboardRow(CCItemStack itemStack) {
    var tooltips = itemStack.getToolTips();
    if (tooltips == null || tooltips.isEmpty() || tooltips.size() < 3) {
      return null;
    }

    var name = tooltips.getFirst().trim();

    var ps = tooltips.get(1).replaceAll("[^0-9]", "").trim();
    if (ps.isEmpty()) {
      return null;
    }
    var position = Integer.parseInt(ps);

    var ss = tooltips.get(2).replaceAll("[^0-9]", "").trim();
    if (ss.isEmpty()) {
      return null;
    }
    var score = Integer.parseInt(ss);

    return new LeaderboardRow(0, name, position, score, itemStack.texture());
  }

  private boolean extractAndTrackTitle(String title) {
    if (!title.contains("Leaderboard")) {
      return false;
    }

    var cleaned = title.replaceAll("[^a-zA-Z0-9s\\(\\) /]", "");
    var gameString = cleaned.substring(0, cleaned.indexOf("Leaderboard")).trim();
    var game = CubepanionAPI.I().tryGame(gameString);
    if (game == null) {
      log.debug("Failed to match {} to a game", gameString);
      return false;
    }

    // Loading new leaderboard
    if (this.currentTrackingGame == null || this.currentTrackingGame.id() != game.id()) {
      this.reset();
    }

    Matcher matcher = pagePattern.matcher(cleaned.trim());
    if (!matcher.matches()) {
      log.warn("Failed to find pages in {}", cleaned);
      return false;
    }

    var currentPage = matcher.group(1);

    int cur;
    try {
      cur = Integer.parseInt(currentPage);
    } catch (NumberFormatException ex) {
      log.error("Failed to parse page number {}", currentPage);
      return false;
    }

    if (this.pages.contains(cur)) {
      return false;
    }

    this.currentPage = cur;
    this.pages.add(this.currentPage);
    this.currentTrackingGame = game;
    return true;
  }

  /**
   * Submits to the API if a valid game has been found and 200 rows are parsed.
   */
  private boolean checkSubmit() {
    if (this.currentTrackingGame == null) {
      log.debug("No game found to submit");
      return false;
    }

    if (this.pages.size() != 10) {
      log.debug("Wanted 10 pages, got {}", this.pages.size());
      return false;
    }

    if (this.rows.size() != 200) {
      this.reset();
      log.debug("10 pages, but not 200 rows, what's going on? Have {}", this.rows.size());
      return false;
    }

    log.info("Going to submit leaderboard for {}", this.currentTrackingGame.displayName());
    return true;
  }

  private void reset() {
    this.currentPage = -1;
    this.currentTrackingGame = null;
    this.pages.clear();
    this.rows.clear();
  }

}
