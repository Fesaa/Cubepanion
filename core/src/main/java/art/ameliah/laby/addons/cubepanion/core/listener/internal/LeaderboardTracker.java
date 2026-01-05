package art.ameliah.laby.addons.cubepanion.core.listener.internal;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.accessors.CCItemStack;
import art.ameliah.laby.addons.cubepanion.core.external.CubepanionAPI;
import art.ameliah.laby.addons.cubepanion.core.external.Game;
import art.ameliah.laby.addons.cubepanion.core.external.LeaderboardRow;
import art.ameliah.laby.addons.cubepanion.core.utils.Colours;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import art.ameliah.laby.addons.cubepanion.core.versionlinkers.FunctionLink;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.gui.screen.ScreenDisplayEvent;
import net.labymod.api.util.logging.Logging;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LeaderboardTracker {

  private final Pattern pagePattern = Pattern.compile(".*\\((\\d+)/\\d+\\)");
  private final Logging log = Logging.create(Cubepanion.class.getSimpleName());
  private final Cubepanion addon;

  // Shared concurrent state
  private final Set<Integer> pages = ConcurrentHashMap.newKeySet();
  private final Set<LeaderboardRow> rows = ConcurrentHashMap.newKeySet(200);

  public LeaderboardTracker(Cubepanion addon) {
    this.addon = addon;
  }

  /** Context per page */
  private record LeaderboardContext(int page, Game game, List<CCItemStack> items) {}

  @Subscribe
  public void onScreenOpen(ScreenDisplayEvent e) {
    if (!this.addon.getManager().onCubeCraft() || !this.addon.getManager().getDivision().equals(CubeGame.LOBBY)) {
      return;
    }

    if (this.addon.getFunctionLink() == null) return;

    this.addon.getFunctionLink()
        .loadMenuContext(
            title -> title != null && title.contains("Leaderboard"),
            items -> items != null && !items.isEmpty()
        )
        .thenApplyAsync(this::createLeaderboardContext)
        .thenApplyAsync(this::processContext)
        .thenComposeAsync(context -> {
          if (context == null) return CompletableFuture.completedFuture(null);

          var submittingFor = context.game();
          var rowsList = rows.stream().toList();

          return CubepanionAPI.I()
              .submit(submittingFor, rowsList)
              .thenAcceptAsync(ignored -> onSuccess(submittingFor));
        })
        .exceptionallyAsync(ex -> {
          log.error("Failed to load or submit leaderboard", ex);
          return null;
        });
  }

  /** Converts a MenuContext into a LeaderboardContext */
  private @Nullable LeaderboardContext createLeaderboardContext(@Nullable FunctionLink.MenuContext menuContext) {
    if (menuContext == null) return null;

    String title = menuContext.title();
    List<CCItemStack> items = menuContext.items();
    if (items == null || items.isEmpty()) return null;

    var cleaned = title.replaceAll("[^a-zA-Z0-9s\\(\\) /]", "");
    var gameString = cleaned.substring(0, cleaned.indexOf("Leaderboard")).trim();
    Game game = CubepanionAPI.I().tryGame(gameString);
    if (game == null) {
      log.debug("Failed to match {} to a game", gameString);
      return null;
    }

    Matcher matcher = pagePattern.matcher(cleaned.trim());
    if (!matcher.matches()) {
      log.warn("Failed to find pages in {}", cleaned);
      return null;
    }

    int curPage;
    try {
      curPage = Integer.parseInt(matcher.group(1));
    } catch (NumberFormatException ex) {
      log.error("Failed to parse page number {}", matcher.group(1));
      return null;
    }

    if (!pages.add(curPage)) return null;

    return new LeaderboardContext(curPage, game, items);
  }

  /** Process items for a page and add rows */
  private @Nullable LeaderboardContext processContext(@Nullable LeaderboardContext context) {
    if (context == null) return null;

    int count = 0;
    for (var item : context.items()) {
      if (item.isAir()) continue;
      if (!item.getAsItem().getIdentifier().getPath().equals("player_head")) continue;

      var row = parseLeaderboardRow(item);
      if (row == null) continue;

      rows.add(row);
      count++;
    }

    log.debug("Adding {} rows from {} items for page {} of {}",
        count, context.items().size(), context.page(), context.game().displayName());

    if (!checkSubmit()) return null;
    return context;
  }

  /** Parse a CCItemStack into a leaderboard row */
  private LeaderboardRow parseLeaderboardRow(CCItemStack itemStack) {
    var tooltips = itemStack.getToolTips();
    if (tooltips == null || tooltips.size() < 3) return null;

    String name = tooltips.getFirst().trim();
    String ps = tooltips.get(1).replaceAll("[^0-9]", "").trim();
    String ss = tooltips.get(2).replaceAll("[^0-9]", "").trim();
    if (ps.isEmpty() || ss.isEmpty()) return null;

    int position = Integer.parseInt(ps);
    int score = Integer.parseInt(ss);

    return new LeaderboardRow(0, name, position, score, itemStack.texture());
  }

  /** Check if we have all rows/pages and ready to submit */
  private boolean checkSubmit() {
    if (pages.size() != 10) {
      log.debug("Wanted 10 pages, got {}", pages.size());
      return false;
    }

    if (rows.size() != 200) {
      log.debug("10 pages, but not 200 rows, have {}", rows.size());
      return false;
    }

    log.info("Ready to submit leaderboard with {} rows", rows.size());
    return true;
  }

  private void onSuccess(Game game) {
    Laby.labyAPI().minecraft().chatExecutor().displayClientMessage(
        Component.translatable(
            "cubepanion.messages.leaderboardAPI.success",
            Component.text(game.displayName())
        ).color(Colours.Success)
    );
    reset();
  }

  private void reset() {
    pages.clear();
    rows.clear();
  }
}
