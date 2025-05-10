package art.ameliah.laby.addons.cubepanion.core.commands;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.external.CubepanionAPI;
import art.ameliah.laby.addons.cubepanion.core.external.Game;
import art.ameliah.laby.addons.cubepanion.core.external.LeaderboardRow;
import art.ameliah.laby.addons.cubepanion.core.utils.Colours;
import art.ameliah.laby.addons.cubepanion.core.utils.I18nNamespaces;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.util.logging.Logging;

public class LeaderboardCommand extends Command {

  private final Logging log = Logging.create(Cubepanion.class.getSimpleName());
  private final Cubepanion addon;
  private final String mainKey = I18nNamespaces.globalNamespace + ".messages.leaderboardAPI.commands.";
  private final Component helpMessage = Component.translatable(this.mainKey + "help.title",
          Colours.Title)
      .append(Component.translatable(this.mainKey + "help.info", Colours.Secondary)
          .decorate(TextDecoration.ITALIC))
      .append(Component.text("\n/leaderboardAPI <userName>", Colours.Primary))
      .append(Component.translatable(this.mainKey + "help.player", Colours.Secondary))
      .append(Component.text("\n/leaderboardAPI ", Colours.Primary))
      .append(Component.text("<game>", Colours.Primary))
      .append(Component.text(" [start]", Colours.Primary))
      .append(Component.translatable(this.mainKey + "help.leaderboard", Colours.Secondary))
      .append(Component.text("\n/leaderboardAPI players", Colours.Primary))
      .append(Component.translatable(this.mainKey + "help.players", Colours.Secondary))
      .append(Component.newline());

  public LeaderboardCommand(Cubepanion addon) {
    super("leaderboard", "lb");

    this.addon = addon;
    this.messagePrefix = addon.prefix();
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (!addon.getManager().onCubeCraft() || arguments.length == 0) {
      return false;
    }

    var name = arguments[0];
    Game game = CubepanionAPI.I().tryGame(name);
    if (game != null) {
      return this.gameLeaderboard(game, arguments);
    }

    if (name.equals("help")) {
      this.displayMessage(this.helpMessage);
      return true;
    }

    if (name.equals("players")) {
      return this.batchLeaderboard();
    }

    return this.playerLeaderboard(name);
  }

  private boolean batchLeaderboard() {
    var players = this.addon.labyAPI().minecraft().clientWorld().getPlayers()
        .stream()
        .map(Player::getName)
        .toList();

    var div = this.addon.getManager().getDivision().getString();
    var game = CubepanionAPI.I().tryGame(div);
    if (game == null) {
      log.warn("Failed to find a game from division? {}", div);
      return true;
    }

    CubepanionAPI.I().batch(game, players)
        .thenAcceptAsync(lb -> {
          Supplier<Component> title = () -> Component.translatable(
                  this.mainKey + "leaderboards.title.game",
                  Component.text(lb.size(), Colours.Secondary),
                  Component.text(game.displayName(), Colours.Secondary).decorate(TextDecoration.BOLD))
              .color(Colours.Primary);
          this.displayPlayerRows(lb, title, LeaderboardRow::player);
        });

    return true;
  }

  private boolean playerLeaderboard(String name) {
    if (!name.matches("[a-zA-Z0-9_]{2,16}")) {
      displayMessage(
          Component.translatable(this.mainKey + "invalidUserName", Component.text(name))
              .color(Colours.Error));
      return true;
    }

    CubepanionAPI.I().getPlayerLeaderboard(name).thenAccept(lb -> {
      Supplier<Component> title = () -> Component.translatable(
              this.mainKey + "leaderboards.title.player",
              Component.text(name, Colours.Secondary).decorate(TextDecoration.BOLD),
              Component.text(lb.leaderboards().size(), Colours.Secondary))
          .color(Colours.Primary);
      this.displayPlayerRows(lb.leaderboards(), title, row -> {
        var game = CubepanionAPI.I().getGameById(row.gameId());
        return game == null ? "Unknown" : game.displayName();
      });
    }).exceptionallyAsync(e -> {
      log.error("Failed to load leaderboard {}", e);
      return null;
    });
    return true;
  }

  private boolean gameLeaderboard(Game game, String[] arguments) {
    int bound;
    try {
      bound = Integer.parseInt(arguments[arguments.length - 1]);
    } catch (NumberFormatException e) {
      bound = 1;
    }
    int bound_2 = Math.min(200, bound + 9);

    final int fBound = bound;
    CubepanionAPI.I().getLeaderboard(game, bound, bound_2)
        .thenAcceptAsync(lb -> {

          if (lb.rows().isEmpty()) {
            this.displayMessage(
                Component.translatable(this.mainKey + "noPlayers",
                        Component.text(game.displayName(), Colours.Secondary)
                            .decorate(TextDecoration.BOLD),
                        Component.text(fBound, Colours.Secondary),
                        Component.text(bound_2, Colours.Secondary))
                    .color(Colours.Primary));
            return;
          }

          Component toDisplay = Component.translatable(this.mainKey + "places.title",
                  Component.text(game.displayName(), Colours.Secondary)
                      .decorate(TextDecoration.BOLD),
                  Component.text(fBound, Colours.Secondary),
                  Component.text(bound_2, Colours.Secondary))
              .color(Colours.Primary);

          for (LeaderboardRow row : lb.rows()) {
            toDisplay = toDisplay.append(
                Component.translatable(this.mainKey + "places.placeInfo",
                    Component.text(row.player()).color(Colours.Primary)
                        .decorate(TextDecoration.BOLD),
                    Component.text(row.position()).color(Colours.Secondary),
                    Component.text(row.score()).color(Colours.Secondary),
                    Component.text(game.scoreType())
                ).color(Colours.Success));
          }

          this.displayMessage(toDisplay);
        }).exceptionallyAsync(e -> {
          log.error("Failed to load leaderboard for {} {}", game.displayName(), e.getMessage());
          return null;
        });
    return true;
  }

  private void displayPlayerRows(List<LeaderboardRow> rows, Supplier<Component> title,
      Function<LeaderboardRow, String> f) {
    if (rows == null) {
      return;
    }

    if (rows.isEmpty()) {
      displayMessage(
          Component.translatable(this.mainKey + "noLeaderboards")
              .color(Colours.Primary));
      return;
    }

    Component toDisplay = title.get();

    for (LeaderboardRow row : rows) {
      var game = CubepanionAPI.I().getGameById(row.gameId());
      String scoreType = game == null ? "Unknown" : game.scoreType();


      toDisplay = toDisplay.append(
          Component.translatable(this.mainKey + "leaderboards.leaderboardInfo",
              Component.text(f.apply(row)).color(Colours.Primary).decorate(TextDecoration.BOLD),
              Component.text(row.position()).color(Colours.Secondary),
              Component.text(row.score()).color(Colours.Secondary),
              Component.text(scoreType)
          ).color(Colours.Success));
    }

    displayMessage(toDisplay);
  }
}
