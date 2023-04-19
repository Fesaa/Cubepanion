package org.cubepanion.core.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.client.component.format.TextDecoration;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.config.imp.GameStatsTracker;
import org.cubepanion.core.config.subconfig.CommandSystemSubConfig;
import org.cubepanion.core.config.subconfig.StatsTrackerSubConfig;
import org.cubepanion.core.managers.CubepanionManager;
import org.cubepanion.core.utils.Colours;
import org.cubepanion.core.utils.CubeGame;
import org.cubepanion.core.utils.I18nNamespaces;

public class StatCommands extends Command {
  private final Cubepanion addon;
  private final Pattern timeFormat = Pattern.compile("\\b(20[0-9]{2})-([1-9]|1[1-2])-(1[0-9]|2[0-9]|3[0-1]|[1-9])\\b");

  private final Function<String, Component> errorComponent = I18nNamespaces.commandNamespaceTransformer("StatCommands.error");
  private final Function<String, String> errorKey = I18nNamespaces.commandNameSpaceMaker("StatCommands.error");
  private final Function<String, Component> helpComponentGetter = I18nNamespaces.commandNamespaceTransformer("StatCommands.helpCommand");

  private final Component gameNotFoundComponent = this.errorComponent.apply("gameNotFound.text").color(Colours.Error)
      .hoverEvent(HoverEvent.showText(this.errorComponent.apply("gameNotFound.text").color(Colours.Hover)));
  private final Component helpComponent = this.helpComponentGetter.apply("title")
      .color(Colours.Title)
      .decorate(TextDecoration.BOLD)

      .append(Component.text("\n/stats <game>", Colours.Primary)
          .clickEvent(ClickEvent.suggestCommand("/stats ")))
      .append(this.helpComponentGetter.apply("displayGlobalStats").color(Colours.Secondary))

      .append(Component.text("\n/stats <game> [username]", Colours.Primary)
          .clickEvent(ClickEvent.suggestCommand("/stats ")))
      .append(this.helpComponentGetter.apply("displayPlayerStats").color(Colours.Secondary))

      .append(Component.text("\n/stats <game> <YYYY-MM-DD>", Colours.Primary)
          .clickEvent(ClickEvent.suggestCommand("/stats ")))
      .append(this.helpComponentGetter.apply("displayGlobalStatsOnDate").color(Colours.Secondary))

      .append(Component.text("\n/stats <game> <YYYY-MM-DD> <username>", Colours.Primary)
          .clickEvent(ClickEvent.suggestCommand("/stats "))
          .hoverEvent(HoverEvent.showText(this.helpComponentGetter.apply("requiredSetting").color(Colours.Hover))))
      .append(this.helpComponentGetter.apply("displayPlayerStatsOnDate").color(Colours.Secondary)
          .hoverEvent(HoverEvent.showText(this.helpComponentGetter.apply("requiredSetting").color(Colours.Hover))))

      .append(Component.text("\n/stats help", Colours.Primary)
          .clickEvent(ClickEvent.suggestCommand("/stats help")))
      .append(this.helpComponentGetter.apply("this").color(Colours.Secondary))

      .append(this.helpComponentGetter.apply("tracking").color(Colours.Primary))
      .append(this.gamesList().color(Colours.Secondary));


  public StatCommands(Cubepanion addon) {
    super("stats");

    this.addon = addon;
    this.messagePrefix = addon.prefix();
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (!this.addon.getManager().onCubeCraft()) {
      return false;
    }
    CommandSystemSubConfig commandSystemSubConfig = this.addon.configuration().getCommandSystemSubConfig();

    if (!commandSystemSubConfig.getStatsCommand().get() || !commandSystemSubConfig.getEnabled().get()) {
      return false;
    }

    if (arguments.length > 0
    && arguments[0].equalsIgnoreCase("help")) {
      this.helpCommand();
      return true;
    }


    CubepanionManager manager = this.addon.getManager();
    StatsTrackerSubConfig config = this.addon.configuration().getStatsTrackerSubConfig();
    HashMap<CubeGame, GameStatsTracker> allGameStatsTrackers = config.getGameStatsTrackers();

    String gameName = this.getGameString(arguments);

    GameStatsTracker gameStatsTracker;
    if (gameName == null) {
       gameStatsTracker = allGameStatsTrackers.get(manager.getDivision());
    } else {
      gameStatsTracker = allGameStatsTrackers.get(CubeGame.stringToGame(gameName));
    }

    if (gameStatsTracker == null) {
      this.displayMessage(this.gameNotFoundComponent.append(this.gamesList().color(Colours.Error)));
      return false;
    }

    if (gameName != null) {
      arguments = this.removeGameNameFromArguments(arguments, gameName);
    }

      switch (arguments.length) {
          case 0 -> {
              this.displayMessage(gameStatsTracker.getDisplayComponent());
              return true;
          }
          case 1 -> {
              if (this.timeFormat.matcher(arguments[0]).matches()) {
                  GameStatsTracker snapShot = gameStatsTracker.getHistoricalData(arguments[0]);
                  if (snapShot == null) {
                      this.displayMessage(Component.translatable(
                                      this.errorKey.apply("statsNotFoundOnDate"),
                                      Component.text(gameStatsTracker.getGame()),
                                      Component.text(arguments[0]))
                              .color(Colours.Error));
                  } else {
                      this.displayMessage(snapShot.getDisplayComponent());
                  }
              } else {
                  this.displayMessage(gameStatsTracker.getUserStatsDisplayComponent(arguments[0]));
              }
              return true;
          }
          case 2 -> {

              String date;
              String userName;

              if (this.timeFormat.matcher(arguments[0]).matches()) {
                  date = arguments[0];
                  userName = arguments[1];
              } else if (this.timeFormat.matcher(arguments[1]).matches()) {
                  date = arguments[1];
                  userName = arguments[0];
              } else {
                  break;
              }

              GameStatsTracker snapShot = gameStatsTracker.getHistoricalData(date);
              if (snapShot == null) {
                  this.displayMessage(Component.translatable(
                                  this.errorKey.apply("statsNotFoundOnDate"),
                                  Component.text(gameStatsTracker.getGame()),
                                  Component.text(date))
                          .color(Colours.Error));
              } else {
                  this.displayMessage(snapShot.getUserStatsDisplayComponent(userName));
              }
          }
      }
    return false;
  }

  private void helpCommand() {
    this.displayMessage(this.helpComponent);
  }

  private Component gamesList() {
    Component comp = Component.empty();
    if (this.addon == null) {
      return comp;
    }
    Set<CubeGame> keySet = this.addon.configuration().getStatsTrackerSubConfig().getGameStatsTrackers().keySet();
    List<CubeGame> keys = new ArrayList<>(keySet);
    for (int i = 0; i < keys.size(); i++) {
      CubeGame game = keys.get(i);
      comp = comp
          .append(Component.text(game.getString())
              .clickEvent(ClickEvent.suggestCommand("/stats " + game + " ")));
      if (i != keys.size() - 1) {
        comp = comp.append(Component.text(", "));
      }
    }
    return comp;
  }

  private String[] removeGameNameFromArguments(String[] arguments, String gameName) {
    String[] gameComponents = gameName.split(" ");

    for (String comp : gameComponents) {
      arguments = this.removeFromArguments(arguments, comp);
    }
    return arguments;
  }

  private String[] removeFromArguments(String[] arguments, String member) {
    String[] newArguments = new String[arguments.length - 1];

    int j = 0;
    for (String argument : arguments) {
      if (!argument.equals(member)) {
        newArguments[j] = argument;
        j++;
      }
    }
    return newArguments;
  }

  private String getGameString(String[] arguments) {
    String game = "";

    for (String arg : arguments) {
      game = (game + " " + arg).trim();

      GameStatsTracker tracker = this.addon.configuration().getStatsTrackerSubConfig().getGameStatsTrackers()
          .get(CubeGame.stringToGame(game));

      if (tracker != null) {
        return game;
      }
    }

    return null;
  }
}
