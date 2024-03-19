package art.ameliah.laby.addons.cubepanion.core.commands;

import static art.ameliah.laby.addons.cubepanion.core.utils.Utils.handleAPIError;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.utils.Colours;
import art.ameliah.laby.addons.cubepanion.core.utils.I18nNamespaces;
import art.ameliah.laby.addons.cubepanion.core.weave.LeaderboardAPI;
import art.ameliah.laby.addons.cubepanion.core.weave.LeaderboardAPI.Leaderboard;
import art.ameliah.laby.addons.cubepanion.core.weave.LeaderboardAPI.LeaderboardRow;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.client.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class LeaderboardAPICommands extends Command {

  private final Cubepanion addon;
  private final String mainKey =
      I18nNamespaces.globalNamespace + ".messages.leaderboardAPI.commands.";
  private final Component helpMessage = Component.translatable(this.mainKey + "help.title",
          Colours.Title)
      .append(Component.translatable(this.mainKey + "help.info", Colours.Secondary)
          .decorate(TextDecoration.ITALIC))
      .append(Component.text("\n/leaderboardAPI <userName>", Colours.Primary))
      .append(Component.translatable(this.mainKey + "help.player", Colours.Secondary))
      .append(Component.text("\n/leaderboardAPI ", Colours.Primary))
      .append(Component.text("<game>", Colours.Primary)
          .hoverEvent(HoverEvent.showText(
              Component.translatable(this.mainKey + "help.clickForMore").color(Colours.Hover)))
          .clickEvent(ClickEvent.runCommand("/lbmappings")))
      .append(Component.text(" [start]", Colours.Primary))
      .append(Component.translatable(this.mainKey + "help.leaderboard", Colours.Secondary))
      .append(Component.text("\n/leaderboardAPI players", Colours.Primary))
      .append(Component.translatable(this.mainKey + "help.players", Colours.Secondary))
      .append(Component.newline());
  private long lastUsed = 0;

  public LeaderboardAPICommands(Cubepanion addon) {
    super("leaderboardAPI", "leaderboard", "lb");
    this.addon = addon;
    this.messagePrefix = addon.prefix();
  }

  private void displayPlayerRows(LeaderboardRow[]rows) {
    if (rows == null) {
      return;
    }

    if (rows.length == 0) {
      displayMessage(
          Component.translatable(this.mainKey + "noLeaderboards")
          .color(Colours.Primary));
      return;
    }

    Component toDisplay = Component.translatable(this.mainKey + "leaderboards.title",
            Component.text(rows[0].player(),
                Colours.Secondary).decorate(TextDecoration.BOLD),
            Component.text(rows.length, Colours.Secondary))
        .color(Colours.Primary);

    for (LeaderboardRow row : rows) {
      toDisplay = toDisplay.append(
          Component.translatable(this.mainKey + "leaderboards.leaderboardInfo",
              Component.text(row.game().displayName()).color(Colours.Primary)
                  .decorate(TextDecoration.BOLD),
              Component.text(row.position()).color(Colours.Secondary),
              Component.text(row.score()).color(Colours.Secondary),
              Component.text(row.game().scoreType())
          ).color(Colours.Success));
    }

    displayMessage(toDisplay);
  }

  private void batchLeaderboard() {
    // Stream should be fine, it's only on command execution.
    // It just looks so much cleaner :rolling_eyes:
    String[] players = this.addon.labyAPI().minecraft().clientWorld().getPlayers()
        .stream()
        .map(Player::getName)
        .toList()
        .toArray(new String[0]);

    LeaderboardAPI.getInstance()
        .getLeaderboardForPlayers(this.addon.getManager().getDivision(), players)
        .exceptionally(throwable -> {
          handleAPIError(getClass(), addon, throwable,
              "Encountered an exception while getting getLeaderboardsForPlayer",
              this.mainKey + "APIError_info",
              this.mainKey + "APIError");
          return null;
        })
        .thenAcceptAsync(this::displayPlayerRows)
        .exceptionally(this::handleError);
  }

  private void playerLeaderboards(String userName) {
    if (!userName.matches("[a-zA-Z0-9_]{2,16}")) {
      displayMessage(
          Component.translatable(this.mainKey + "invalidUserName", Component.text(userName))
              .color(Colours.Error));
      return;
    }

    LeaderboardAPI.getInstance()
        .getLeaderboardsForPlayer(userName)
        .exceptionally(throwable -> {
          handleAPIError(getClass(), addon, throwable,
              "Encountered an exception while getting getLeaderboardsForPlayer",
              this.mainKey + "APIError_info",
              this.mainKey + "APIError");
          return null;
        })
        .thenAcceptAsync(this::displayPlayerRows)
        .exceptionally(this::handleError);
  }

  private void gameLeaderboard(String last, Leaderboard leaderboard) {
    int bound;
    try {
      bound = Integer.parseInt(last);
    } catch (NumberFormatException e) {
      bound = 1;
    }
    int bound_2 = Math.min(200, bound + 9);

    int finalBound = bound;
    LeaderboardAPI.getInstance()
        .getGameLeaderboard(leaderboard, bound, bound_2)
        .exceptionally(throwable -> {
          handleAPIError(getClass(), addon, throwable,
              "Encountered an exception while getting getLeaderboardsForPlayer",
              this.mainKey + "APIError_info",
              this.mainKey + "APIError");
          return null;
        })
        .thenAcceptAsync(rows -> {
          if (rows == null) {
            return;
          }

          if (rows.length == 0) {
            this.displayMessage(
                Component.translatable(this.mainKey + "noPlayers",
                        Component.text(leaderboard.displayName(), Colours.Secondary)
                            .decorate(TextDecoration.BOLD),
                        Component.text(finalBound, Colours.Secondary),
                        Component.text(bound_2, Colours.Secondary))
                    .color(Colours.Primary));
            return;
          }

          Component toDisplay = Component.translatable(this.mainKey + "places.title",
                  Component.text(leaderboard.displayName(), Colours.Secondary)
                      .decorate(TextDecoration.BOLD),
                  Component.text(finalBound, Colours.Secondary),
                  Component.text(bound_2, Colours.Secondary))
              .color(Colours.Primary);

          for (LeaderboardRow row : rows) {
            toDisplay = toDisplay.append(
                Component.translatable(this.mainKey + "places.placeInfo",
                    Component.text(row.player()).color(Colours.Primary)
                        .decorate(TextDecoration.BOLD),
                    Component.text(row.position()).color(Colours.Secondary),
                    Component.text(row.score()).color(Colours.Secondary),
                    Component.text(leaderboard.scoreType())
                ).color(Colours.Success));
          }
          displayMessage(toDisplay);
        }).exceptionally(this::handleError);
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (!this.addon.getManager().onCubeCraft()
        || !this.addon.configuration().getLeaderboardAPIConfig().getUserCommands().get()) {
      return false;
    }

    if (arguments.length == 0) {
      displayMessage(this.helpMessage);
      return true;
    }

    long now = System.currentTimeMillis();
    if (now - this.lastUsed < 5000) {
      displayMessage(Component.translatable(this.mainKey + "coolDown",
          Component.text(5 - (now - this.lastUsed) / 1000,
              NamedTextColor.DARK_RED)).color(Colours.Error));
      return true;
    }
    this.lastUsed = now;

    Leaderboard leaderboard = this.separateLeaderboardAndUserName(arguments);

    if (arguments.length == 1 && leaderboard == null) { // User leaderboards
      if (arguments[0].equalsIgnoreCase("players")) {
        batchLeaderboard();
        return true;
      }

      playerLeaderboards(arguments[0]);
      return true;
    }

    if (leaderboard == null) {
      this.displayMessage(Component.translatable(this.mainKey + "invalidLeaderBoard",
              Component.text(String.join(" ", arguments)))
          .color(Colours.Error));
      return true;
    }

    gameLeaderboard(arguments[arguments.length - 1], leaderboard);
    return true;
  }

  private @Nullable LeaderboardAPI.Leaderboard separateLeaderboardAndUserName(String[] arguments) {
    Leaderboard lb;
    String tryForLeaderboard = "";

    for (String s : arguments) {
      tryForLeaderboard = (tryForLeaderboard + " " + s).trim();
      lb = LeaderboardAPI.getInstance().getLeaderboard(tryForLeaderboard);
      if (lb != null) {
        return lb;
      }
    }
    return null;
  }

  private Void handleError(Throwable throwable) {
    handleAPIError(getClass(), addon, throwable,
        "Encountered an exception during command execution",
        this.mainKey + "APIError_info",
        this.mainKey + "APIError");
    return null;
  }

}
